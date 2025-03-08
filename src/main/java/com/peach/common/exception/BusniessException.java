package com.peach.common.exception;

import com.peach.common.enums.StatusEnum;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/2/26 14:18
 */
public class BusniessException extends RuntimeException{

    private final String code;

    public BusniessException(String code,String message) {
        super(message);
        this.code = code;
    }

    public BusniessException(StatusEnum statusEnum, String message) {
        super(message);
        this.code = statusEnum.getCode();
    }

    public BusniessException(StatusEnum statusEnum) {
        super(statusEnum.getMsg());
        this.code = statusEnum.getCode();
    }
}
