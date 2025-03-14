package com.peach.common.log;

import java.util.List;
import java.util.Map;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/15 0:16
 */
public abstract class AbstractUserService {


    protected abstract void batchInsertOperLog(List<Map<String,Object>> userOperLogList);

    protected abstract void batchUpdateLoginLog(List<Map<String,Object>> LoginLogList);
}
