package com.peach.common.dao;

import com.peach.common.BaseDao;
import com.peach.common.anno.MyBatisDao;
import com.peach.common.entity.UserOperLogDO;
import org.springframework.stereotype.Indexed;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/3/14 23:14
 */
@Indexed
@MyBatisDao
public interface UserOperLogDao extends BaseDao<UserOperLogDO> {


}
