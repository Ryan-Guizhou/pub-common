package com.peach.common;

import java.util.List;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2024/10/14 15:51
 */
public interface BaseDao<T>{

    public void insert(T t);

    public void batchInsert(List<T> list);

    public void update(T t);

    public void updateById(T t);

    public void del(T t);

    public void delById(String id);

    public void delByIds(List<String> ids);

    public int count(T t);

    public T selectById(String id);

    public List<T> selectByIds(List<String> ids);

    public List<T> select(T t);

}
