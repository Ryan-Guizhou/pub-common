package com.peach.common.util.unique;


import com.peach.common.enums.IDStrategy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SnowflakeIDGenerator  extends AbstractIDGenerator {
    private static final long twepoch = 1288834974657L;
    private static final long workerIdBits = 5L;
    private static final long datacenterIdBits = 5L;
    private static final long maxWorkerId = 31L;
    private static final long maxDatacenterId = 31L;
    private static final long sequenceBits = 12L;
    private static final long workerIdShift = 12L;
    private static final long datacenterIdShift = 17L;
    private static final long timestampLeftShift = 22L;
    private static final long sequenceMask = 4095L;
    private static long lastTimestamp = -1L;
    private static long MAX_BACKWARD_MS = 10L;
    private long sequence = 0L;
    private final long workerId;
    private final long datacenterId;

    public SnowflakeIDGenerator() {
        this(1L,1L);
    }

    private SnowflakeIDGenerator(long workerId, long datacenterId) {
        if ((workerId > 31L) || (workerId < 0L)) {
            throw new IllegalArgumentException("worker Id can't be greater than %d or less than 0");
        }
        if ((datacenterId > 31L) || (datacenterId < 0L)) {
            throw new IllegalArgumentException("datacenter Id can't be greater than %d or less than 0");
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    @Override
    public IDStrategy strategy() {
        return IDStrategy.SNOWFLAKE;
    }

    @Override
    public String doGenerateId() {
        long timestamp = timeGen();
        timestamp = handleClockBlack(timestamp);
        if (lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1L & 0xFFF);
            if (this.sequence == 0L) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            this.sequence = 0L;
        }
        lastTimestamp = timestamp;
        long nextId = timestamp - 1288834974657L << 22 | this.datacenterId << 17 | this.workerId << 12 | this.sequence;
        return String.valueOf(nextId);
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 解决始终回拨问题
     * @param currentTimestamp
     * @return
     */
    private long handleClockBlack (long currentTimestamp){
        if (currentTimestamp < lastTimestamp) {
            long offset = lastTimestamp - currentTimestamp;
            if (offset <= MAX_BACKWARD_MS) {
                // 等待时钟追上
                try {
                    Thread.sleep(offset);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    String msg = "Clock moved backwards. Refusing to generate id";
                    log.error(msg + e.getMessage(), e);
                    throw new RuntimeException(msg, e);
                }
                currentTimestamp = timeGen();
            } else {
                String msg = String.format("Clock moved backwards too much. Refusing to generate id for %d milliseconds", offset);
                log.error(msg);
                throw new RuntimeException(msg);
            }
        }
        return currentTimestamp;
    }



}