package com.peach.common.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peach.common.IMongoDao;
import com.peach.common.util.PeachCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
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
@ConditionalOnProperty(prefix = "mongo", name = "enabled", havingValue = "true")
public class MongoUserService extends AbstractUserService {

    @Resource
    private IMongoDao mongoDao;

    private final ObjectMapper _MAPPER = new ObjectMapper();

    /**
     * 操作日志 mongo存储日志表名
     */
    private static final String OPER_LOG_COLLECTION_NAME = "user_oper_log";

    /**
     * 登录日志
     */
    private static final String LOGIN_LOG_COLLECTION_NAME = "login_log";

    @Override
    protected void batchInsertOperLog(List<Map<String,Object>> userOperLogList) {
        if(PeachCollectionUtil.isEmpty(userOperLogList)){
            log.error("操作日志需要插入的数据为空,跳过本次执行");
            return;
        }
        List<Document> operLogDOList = userOperLogList.stream().map(Document::new).collect(Collectors.toList());
        log.error("本次插入的操作日志数据条数为:[{}]",operLogDOList);
        mongoDao.insertMany(OPER_LOG_COLLECTION_NAME,operLogDOList);
    }

    @Override
    protected void batchUpdateLoginLog(List<Map<String, Object>> LoginLogList) {
        if(PeachCollectionUtil.isEmpty(LoginLogList)){
            log.error("登录日志需要插入的数据为空,跳过本次执行");
            return;
        }
        List<Document> loginLogList = LoginLogList.stream().map(Document::new).collect(Collectors.toList());
        log.error("本次插入的登录日志数据条数为:[{}]",loginLogList);
        mongoDao.insertMany(LOGIN_LOG_COLLECTION_NAME,loginLogList);
    }
}
