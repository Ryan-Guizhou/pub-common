package com.peach.common.log;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.peach.common.IMongoDao;
import com.peach.common.constant.PubCommonConst;
import com.peach.common.constant.TableNameContant;
import com.peach.common.entity.LoginLogDO;
import com.peach.common.entity.UserOperLogDO;
import com.peach.common.entity.qo.LogQO;
import com.peach.common.util.DateUtil;
import com.peach.common.util.PeachCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
public class MongoLogService extends AbstractLogService {

    @Resource
    private IMongoDao mongoDao;

    private final ObjectMapper _MAPPER = new ObjectMapper();


    @Override
    protected void batchInsertOperLog(List<Map<String,Object>> userOperLogList) {
        if(PeachCollectionUtil.isEmpty(userOperLogList)){
            log.error("操作日志需要插入的数据为空,跳过本次执行");
            return;
        }
        List<Document> operLogDOList = userOperLogList.stream().map(Document::new).collect(Collectors.toList());
        log.error("本次插入的操作日志数据条数为:[{}]",operLogDOList.size());
        mongoDao.insertMany(TableNameContant.OPER_LOG_COLLECTION_NAME,operLogDOList);
    }

    @Override
    protected void batchInsertLoginLog(List<Map<String, Object>> LoginLogList) {
        if(PeachCollectionUtil.isEmpty(LoginLogList)){
            log.error("登录日志需要插入的数据为空,跳过本次执行");
            return;
        }
        List<Document> loginLogList = LoginLogList.stream().map(Document::new).collect(Collectors.toList());
        log.error("本次插入的登录日志数据条数为:[{}]",loginLogList.size());
        mongoDao.insertMany(TableNameContant.LOGIN_LOG_COLLECTION_NAME,loginLogList);
    }

    @Override
    public PageInfo selectOperLog(LogQO logQO) {
        log.info("操作日志分页查询参数为:[{}]", JSONUtil.toJsonStr(logQO));
        PageInfo pageList = selectLogList(logQO,TableNameContant.OPER_LOG_COLLECTION_NAME, UserOperLogDO.class);
        return pageList;
    }

    @Override
    public PageInfo selectLoginLog(LogQO logQO) {
        log.info("登录日志分页查询参数为:[{}]", JSONUtil.toJsonStr(logQO));
        PageInfo pageList = selectLogList(logQO,TableNameContant.LOGIN_LOG_COLLECTION_NAME, LoginLogDO.class);
        return pageList;
    }

    /**
     * 封装查询日志的方法
     * @param logQO
     * @param collectionName
     * @param clazz
     * @return
     */
    private PageInfo selectLogList(LogQO logQO,String collectionName,Class<?> clazz) {
        //1、构建查询条件
        Document query = new Document();
        Optional.ofNullable(logQO.getOptLevel()).ifPresent(optLevel -> query.put("optLevel", optLevel));
        Optional.ofNullable(logQO.getModuleCode()).ifPresent(moduleCode -> query.put("moduleCode", moduleCode));
        Optional.ofNullable(logQO.getOptTypeCode()).ifPresent(optTypeCode -> query.put("optTypeCode", optTypeCode));
        Optional.ofNullable(logQO.getIsSuccess()).ifPresent(isSuccess -> query.put("isSuccess", isSuccess));
        Optional.ofNullable(logQO.getIsSuccess()).ifPresent(isSuccess -> query.put("isSuccess", isSuccess));

        //1.1、构建时间区间查询
        Document timeCondition = new Document();
        Optional.ofNullable(logQO.getStartTime()).ifPresent(startTime -> timeCondition.put("$gte", DateUtil.parseDate(startTime)));
        Optional.ofNullable(logQO.getEndTime()).ifPresent(endTime -> timeCondition.put("$lte", DateUtil.parseDate(endTime)));
        Optional.of(timeCondition).ifPresent(condition -> query.put("timeCondition", condition));

        //2、构建排序方式方式
        Document sort = new Document();
        Optional.ofNullable(logQO.getOrderType()).ifPresent(orderType -> {
            Integer order = orderType.equalsIgnoreCase(PubCommonConst.ORDER_TYPE_DESC) ? 1 : -1;
            sort.put("order", order);
        });


        //3、构建映射关系,查询那些字段 那些字段不查询等
        Document projection = new Document();
        projection.append("_id", 0);

        //4、构建分页参数
        PageInfo<Object> pageInfo = new PageInfo<>();
        pageInfo.setPageNum(logQO.getPageNum());
        pageInfo.setPageSize(logQO.getPageSize());

        //5、触发分页查询
        PageInfo pageList = mongoDao.findPageList(collectionName, query, sort, projection, pageInfo, clazz);
        return pageList;
    }
}
