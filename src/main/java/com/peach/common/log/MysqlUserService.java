package com.peach.common.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peach.common.dao.LoginLogDao;
import com.peach.common.dao.UserOperLogDao;
import com.peach.common.entity.LoginLogDO;
import com.peach.common.entity.UserOperLogDO;
import com.peach.common.util.PeachCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/15 0:18
 */
@Slf4j
@Indexed
@Service
@ConditionalOnProperty(prefix = "mysql", name = "enabled", havingValue = "false", matchIfMissing = true)
public class MysqlUserService extends AbstractUserService {

    @Resource
    private UserOperLogDao userOperLogDao;

    @Resource
    private LoginLogDao loginLogDao;

    private final ObjectMapper _MAPPER = new ObjectMapper();

    @Override
    protected void batchInsertOperLog(List<Map<String,Object>> userOperLogList) {
        if(PeachCollectionUtil.isEmpty(userOperLogList)){
            log.error("操作日志需要插入的数据为空,跳过本次执行");
            return;
        }
        List<UserOperLogDO> operLogDOList = userOperLogList.stream().map(userOperLog -> {
            UserOperLogDO userOperLogDO = _MAPPER.convertValue(userOperLog, UserOperLogDO.class);
            return userOperLogDO;
        }).collect(Collectors.toList());
        log.error("操作日志本次插入的数据条数为:[{}]",operLogDOList);
        userOperLogDao.batchInsert(operLogDOList);
    }

    @Override
    protected void batchUpdateLoginLog(List<Map<String, Object>> LoginLogList) {
        if(PeachCollectionUtil.isEmpty(LoginLogList)){
            log.error("登录日志需要插入的数据为空,跳过本次执行");
            return;
        }
        List<LoginLogDO> operLogDOList = LoginLogList.stream().map(loginLog -> {
            LoginLogDO loginLogDO = _MAPPER.convertValue(loginLog, LoginLogDO.class);
            return loginLogDO;
        }).collect(Collectors.toList());
        log.error("登录日志本次插入的数据条数为:[{}]",operLogDOList);
        loginLogDao.batchInsert(operLogDOList);
    }
}
