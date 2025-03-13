package com.peach.common;

import com.github.pagehelper.PageInfo;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Set;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2024/10/15 15:34
 */
public interface IMongoDao<T> {

    /**
     * 获取集合对象
     *
     * @param collectionName 集合名称
     * @return MongoCollection<Document>
     */
    MongoCollection<Document> getDBCollection(String collectionName);

    /**
     * 查询所有的集合名称
     *
     * @return Set<String>
     */
    Set<String> collectionSet();


    /**
     * 判断集合是否存在
     *
     * @param collectionName 集合名称
     * @return boolean
     */
    boolean collectionExists(String collectionName);

    /**
     * 根据集合名称创建集合对象
     *
     * @param collectionName 集合名称
     * @return MongoCollection<Document>
     */
    MongoCollection<Document> createCollection(String collectionName);

    /**
     * 创建索引
     *
     * @param collectionName 集合名称
     * @param document       索引文档记录
     */
    void createIndex(String collectionName, Document document);

    /**
     * 删除索引
     *
     * @param collectionName 集合名称
     * @param document       索引文档记录
     */
    void dropIndex(String collectionName, Document document);

    /**
     * 根据集合名称删除集合对象
     *
     * @param collectionName 集合名称
     */
    void dropCollection(String collectionName);

    /**
     * 查询Document对象列表
     *
     * @param collectionName 集合名称
     * @param query          查询条件
     * @return List<Document>
     */
    List<Document> findList(String collectionName, Document query);

    /**
     * 对某一字段进行排序查询
     *
     * @param query   查询条件
     * @param orderBy 排序字段
     * @return List<Document>
     */
    List<Document> sort(String collectionName, Document query, Document orderBy);

    /**
     * 查询所有结果
     *
     * @param collectionName 集合名称
     * @return List<Document>
     */
    List<Document> find(String collectionName);

    /**
     * 查询数量
     *
     * @param collectionName 集合名称
     * @return
     */
    long count(String collectionName);

    /**
     * 查询数量
     *
     * @param collectionName 集合名称
     * @param query
     * @return
     */
    long count(String collectionName, Document query);
    
    /**
     * 根据query查询单个文档
     *
     * @param collectionName 集合名称
     * @param query          查询参数对象
     * @return Document 根据查询条件查找的对象
     */
    Document findOne(String collectionName, Document query);

    /**
     * 根据query查询单个文档,并删除该对象
     *
     * @param collectionName 集合名称
     * @param query          查询参数对象
     * @return Document 根据查询条件查找的对象
     */
    Document findOneAndDelete(String collectionName, Document query);

    /**
     * 查询单个Mongo数据对象
     *
     * @param collectionName 集合名称
     * @param query          查询参数对象
     * @param update         修改对象
     * @return Document 根据查询条件查找的对象
     */
    Document findOneAndUpdate(String collectionName, Document query, Document update);

    /**
     * 根据query查询单个文档,并根据replacement更新一条文档(replacement中的属性覆盖更新原有的数据)
     *
     * @param collectionName 集合名称
     * @param query          查询参数对象
     * @param replacement    覆盖更新对象
     * @return Document 根据查询条件查找的对象
     */
    Document findOneAndReplace(String collectionName, Document query, Document replacement);


    /**
     * 根据分页对象获取分页列表数据
     * 1、支持query的所有方式的查询条件
     * 2、排序
     * 3、投影
     * 4、分页
     * 5、转成对应的实体等
     * @param collectionName 集合名称
     * @param query          查询条件
     * @param sort           排序方式
     * @param projection     查询字段
     * @param pageInfo       分页参数
     * @param clz            转换对象
     * @return
     */
    PageInfo<T> findPageList(String collectionName, Document query, Document sort, Document projection, PageInfo<T> pageInfo, Class<T> clz);


    /**
     * 查询符合条件的数据
     * 1、查询条件
     * 2、排序
     * 3、投影
     * 4、转成对应的实体等
     * @param collectionName 集合名称
     * @param query          查询条件
     * @param sort           排序方式
     * @param projection     查询字段
     * @param clz            转换对象
     * @return
     */
    List<T> findList(String collectionName, Document query, Document sort, Document projection, Class<T> clz);

    /**
     * 查询符合条件的数据
     * 1、查询条件
     * 2、排序
     * 3、投影
     * @param collectionName 集合名称
     * @param query          查询条件
     * @param sort           排序方式
     * @param projection     查询字段
     * @return
     */
    List<Document> findList(String collectionName, Document query, Document sort, Document projection);

    /**
     * 根据replacement覆盖更新一条文档(replacement中的属性覆盖更新原有的数据;如果存在多条,根据顺序更新第一条记录)
     *
     * @param collectionName 集合名称
     * @param query          查询参数对象
     * @param replacement    覆盖更新对象
     * @return long 替换成功的文档个数---> 大于等于0:替换成功;等于-1:替换失败
     */
    long replaceOne(String collectionName, Document query, Document replacement);

    /**
     * 批量插入
     *
     * @param collectionName 集合名称
     * @param documentLst    文档列表
     * @return boolean true:成功;false:失败
     */
    boolean insertMany(String collectionName, List<Document> documentLst);

    /**
     * 插入一条记录
     *
     * @param collectionName 集合名称
     * @param document       一条文档
     * @return boolean true:成功;false:失败
     */
    boolean insert(String collectionName, Document document);

    /**
     * 更新一条文档(如果存在多条,根据顺序修改第一条记录)
     *
     * @param collectionName 集合名称
     * @param query          查询条件
     * @param document       更新文档
     * @return long 替换成功的文档个数---> 大于等于0:修改成功;等于-1:修改失败
     */
    long updateOne(String collectionName, Document query, Document document);

    /**
     * 更新多条文档
     *
     * @param collectionName 集合名称
     * @param query          查询条件
     * @param document       更新文档
     * @return long 替换成功的文档个数---> 大于等于0:修改成功;等于-1:修改失败
     */
    long updateMany(String collectionName, Document query, Document document);

    /**
     * 根据_id批量更新
     *
     * @param collectionName 集合名称
     * @param queryArgs      查询_id参数列表
     * @param document       更新文档
     * @return long 替换成功的文档个数---> 大于等于0:修改成功;等于-1:修改失败
     */
    long updateBatchId(String collectionName, List<ObjectId> queryArgs, Document document);

    /**
     * 删除一条文档(如果存在多条,根据顺序删除第一条记录)
     *
     * @param collectionName 集合名称
     * @param query          查询条件
     * @return long 替换成功的文档个数---> 大于等于0:修改成功;等于-1:修改失败
     */
    long deleteOne(String collectionName, Document query);

    /**
     * 删除多条文档
     *
     * @param collectionName 集合名称
     * @param query          查询条件
     * @return long 替换成功的文档个数---> 大于等于0:修改成功;等于-1:修改失败
     */
    long deleteMany(String collectionName, Document query);

    /**
     * 查询Document对象列表
     *
     * @param collectionName 集合名称
     * @param query          查询条件
     * @param projection     查询指定字段
     * @return List<Document>
     */
    List<Document> findList(String collectionName, Document query, Document projection);

    /**
     *
     * @param collectionName 集合名称
     * @param query          查询条件
     * @param projection     查询指定字段
     * @param skip           跳过skip条
     * @param limit          抽取limit条
     * @return
     */
    List<Document> findList(String collectionName, Document query, Document projection, int skip, int limit);
}
