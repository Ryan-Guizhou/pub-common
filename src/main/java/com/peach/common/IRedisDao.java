package com.peach.common;

import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2024/10/10 15:12
 */
public interface IRedisDao {
    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return boolean
     */
    boolean existsKey(Object key);

    /**
     * 根据key获取key列表(key值可为模糊匹配---taskInfo:taskDetail:* <---> *代表任意字符)
     *
     * @param pattern
     * @return Set<Object>
     */
//	Set<Object> keys(Object pattern);

    /**
     * 根据key删除对应的value
     *
     * @param key
     */
    boolean delete(Object key);

    /**
     * 根据key获取个数
     *
     * @param key
     */
//	int count(Object key);

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    void delete(String[] keys);

    /**
     * 批量删除key(key值可为模糊匹配---taskInfo:taskDetail:* <---> *代表任意字符)
     *
     * @param pattern
     */
    long deletePattern(Object pattern, Integer count);

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    long delete(Set keys);

    /**
     * 写入缓存(操作字符串)
     *
     * @param key
     * @param value
     * @return boolean
     */
    boolean vSet(Object key, Object value);

    /**
     * 写入缓存设置时效时间(操作字符串)
     *
     * @param key
     * @param value
     * @return boolean
     */
    boolean vSet(Object key, Object value, Long expireTime);

    /**
     * 写入缓存设置时效时间(操作字符串)
     *
     * @param key
     * @param value
     * @return boolean
     */
    boolean vSet(Object key, Object value, Duration expire);

    /**
     * 更新写入缓存设置时效时间(操作字符串)
     *
     * @param key
     * @return boolean
     */
    boolean vSetUpdate(Object key, Long expireTime);

    /**
     * 读取缓存(操作字符串)
     *
     * @param key
     * @return Object
     */
    Object vGet(Object key);

    /**
     * 哈希 添加(操作hash)
     *
     * @param key
     * @param hashKey
     * @param value
     */
    void hmSet(Object key, Object hashKey, Object value);

    /**
     * 哈希 添加(操作hash)
     *
     * @param key
     * @param map
     */
    void hmSetAll(Object key, Map<Object, Object> map);

    /**
     * 哈希获取数据(操作hash)
     *
     * @param key
     * @return Map<Object, Object>
     */
    Map<Object, Object> hmGet(Object key);

    /**
     * 哈希获取数据(操作hash)
     *
     * @param key
     * @param hashKey
     * @return Object
     */
    Object hmGet(Object key, Object hashKey);

    /**
     * 哈希删除数据(操作hash)
     *
     * @param key
     * @param hashKey
     * @return Object
     */
    Object hmDel(Object key, Object hashKey);

    /**
     * 获取列表中个数
     *
     * @param k
     * @return long
     */
    long lSize(Object k);

    /**
     * 获取列表左边的第一个元素(操作list)
     *
     * @param key
     * @return Object
     */
    Object lLeftIndexFirst(Object key);

    /**
     * 获取列表右边的第一个元素(操作list)
     *
     * @param key
     * @return Object
     */
    Object lRightIndexFirst(Object key);

    /**
     * 通过其索引从列表获取元素(操作list)
     *
     * @param key
     * @param index:索引位置,从0开始
     * @return Object
     */
    Object lindex(Object key, long index);

    /**
     * 从左向右添加列表(操作list)
     *
     * @param k
     * @param v
     */
    void lLeftPush(Object k, Object v);

    /**
     * 从左向右添加列表(操作list);如果bool=true,会删除列表中已经存在的数据,然后再进行添加(仅针对字符串列表,其它待测)
     *
     * @param k
     * @param v
     * @param bool
     */
    void lLeftPush(Object k, Object v, boolean bool);

    /**
     * 从左向右添加列表(操作list)
     *
     * @param k
     * @param lst
     */
    void lLeftPushAll(Object k, List<Object> lst);

    /**
     * 从右向左添加列表(操作list)
     *
     * @param k
     * @param v
     */
    void lRightPush(Object k, Object v);

    /**
     * 从右向左添加列表(操作list);如果bool=true,会删除列表中已经存在的数据,然后再进行添加(仅针对字符串列表,其它待测)
     *
     * @param k
     * @param v
     * @param bool
     */
    void lRightPush(Object k, Object v, boolean bool);

    /**
     * 从右向左添加列表(操作list)
     *
     * @param k
     * @param lst
     */
    void lRightPushAll(Object k, List<Object> lst);

    /**
     * 删除并获取列表中的第1个元素(操作list)
     *
     * @param k
     * @return Object
     */
    Object lLeftPop(Object k);

    /**
     * 删除并获取列表中的最后1个元素(操作list)
     *
     * @param k
     * @return Object
     */
    Object lRightPop(Object k);

    /**
     * 移除k中的count个,返回删除的个数；如果没有这个元素则返回0(操作list)
     *
     * @param k
     * @param count
     * @return long
     */
    long lRemove(Object k, long count);

    /**
     * 移除k中值为v的count个,返回删除的个数；如果没有这个元素则返回0(操作list)
     *
     * @param k
     * @param count
     * @param v
     * @return long
     */
    long lRemove(Object k, long count, Object v);

    /**
     * 移除k中值为v的所有数据,返回删除的个数；如果没有这个元素则返回0(操作list)
     * @param k
     * @param v
     * @return
     */
    long lRemove(Object k, Object v);

    /**
     * 根据key获取获取List列表(操作list)
     *
     * @param key
     * @return Object
     */
    Object lRange(Object key);

    /**
     * 根据key获取列表中第start至end的数据(操作list)
     *
     * @param k
     * @param start
     * @param end
     * @return List<Object>
     */
    List<?> lRange(Object k, long start, long end);

    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    void sAdd(Object key, Object value);

    /**
     * 集合获取
     *
     * @param key
     * @return Set<Object>
     */
    Set<Object> sMembers(Object key);

    /**
     * 随机获取变量中的元素
     *
     * @param key 键
     * @return
     */
    Object sRandomMember(String key);

    /**
     * 弹出变量中的元素
     *
     * @param key 键
     * @return
     */
    Object sPop(String key);

    /**
     * 批量移除set缓存中元素
     *
     * @param key    键
     * @param values 值
     * @return
     */
    void sRemove(String key, Object... values);

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    void zAdd(Object key, Object value, double scoure);

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return Set<Object>
     */
    Set<Object> rangeByScore(Object key, double scoure, double scoure1);

    /**
     * 将hashKey中储存的数字加上指定的增量值(操作hash)
     *
     * @param key
     * @param value
     * @return boolean
     */
    void hmSetIncrement(Object key, Object hashKey, Long value);

    /**
     * 消息队列pub方式推送
     *
     * @param topic
     * @param message
     * @return boolean
     */
    void convertAndSend(String topic, String message);

    /**
     * 消息队列pub方式推送
     *
     * @param topic
     * @param message
     * @return boolean
     */
    void convertAndSend(String topic, Object message);

    /**
     * 支持单机和集群
     *
     * @param matchKey 匹配字段
     * @param count    获取条数
     * @return
     */
    Set<Object> scan(String matchKey, Integer count);

    /**
     * 只支持单机redis
     *
     * @param matchKey 匹配字段
     * @param count    获取条数
     * @return
     */
    @Deprecated
    Map hscan(String matchKey, Integer count);

    /**
     * 只支持单机redis
     *
     * @param matchKey 匹配字段
     * @param count    获取条数
     * @return
     */
    @Deprecated
    Set<String> sscan(String matchKey, Integer count);

    /**
     * 只支持单机redis
     *
     * @param matchKey 匹配字段
     * @param count    获取条数
     * @return
     */
    @Deprecated
    Map zscan(String matchKey, Integer count);

    /**
     * 设置序列化
     *
     * @param serializer
     */
    void setKeySerializer(StringRedisSerializer serializer);

    Set<Object> keys(String matchKey);

    void expire(Object k, long timeout);

    /**
     * 获取自增数据
     *
     * @param key
     * @param fromIndex
     * @return: long
     * @author: pc
     */
    long increaseNum(String key, long fromIndex);
    /**
     * 递减数
     *
     * @param key
     * @param decIndex
     * @return: long
     * @author: pc
     */
    long decreaseNum(String key, long decIndex);

    /**
     * 查看某个key的过期时间
     * @param key
     * @return
     */
    int ttlKey(String key);
}
