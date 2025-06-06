package com.peach.common.dao;


import com.peach.common.BaseDao;
import com.peach.common.anno.MyBatisDao;
import com.peach.common.entity.qo.LogQO;
import com.peach.common.entity.LoginLogDO;
import org.springframework.stereotype.Indexed;

import java.util.List;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/15 2:00
 */
@Indexed
@MyBatisDao
public interface LoginLogDao extends BaseDao<LoginLogDO> {

    List<LoginLogDO> selectByQO(LogQO logQO);

}
