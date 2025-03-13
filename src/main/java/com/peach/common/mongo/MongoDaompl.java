package com.peach.common.mongo;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.peach.common.IMongoDao;
import com.peach.common.util.DateUtil;
import com.peach.common.util.PeachCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description 所有的查询都遵循
 *  1、将 _id转成id 为主键
 *  2、将时间类型转成 Date类型转成String类型 避免需要转对象时报错
 * @CreateTime 2025/3/13 16:45
 */
@Slf4j
@Indexed
@Service
public class MongoDaompl<T> extends AbstractMongoService implements IMongoDao<T> {

    @Override
    public MongoCollection<Document> getDBCollection(String collectionName) {
        return COLLECTION_CACHE.get(collectionName, key -> {
            MongoCollection<Document> collection = mongoTemplate.getCollection(collectionName);
            return collection;
        });
    }

    @Override
    public Set<String> collectionSet() {
        return mongoTemplate.getCollectionNames();
    }

    @Override
    public boolean collectionExists(String collectionName) {
        return mongoTemplate.collectionExists(collectionName);
    }

    @Override
    public MongoCollection<Document> createCollection(String collectionName) {
        return mongoTemplate.createCollection(collectionName);
    }

    @Override
    public void createIndex(String collectionName, Document document) {
        getDBCollection(collectionName).createIndex(document);
    }

    @Override
    public void dropIndex(String collectionName, Document document) {
        getDBCollection(collectionName).dropIndex(document);
    }

    @Override
    public void dropCollection(String collectionName) {
        mongoTemplate.dropCollection(collectionName);
    }

    @Override
    public List<Document> findList(String collectionName, Document query) {
        return findList(collectionName, query,new Document(),new Document());
    }

    @Override
    public List<Document> sort(String collectionName, Document query, Document orderBy) {
        return findList(collectionName, query,orderBy,new Document());
    }

    @Override
    public List<Document> find(String collectionName) {
        List<Document> resultList = new ArrayList<>();
        try (MongoCursor<Document> cursor = getDBCollection(collectionName)
                .find()
                .iterator()) {  // 使用 try-with-resources 确保游标关闭
            return improveDocuments(resultList, cursor);
        }
    }

    @Override
    public long count(String collectionName) {
        return getDBCollection(collectionName).estimatedDocumentCount();
    }

    @Override
    public long count(String collectionName, Document query) {
        return getDBCollection(collectionName).countDocuments(query);
    }


    @Override
    public Document findOne(String collectionName, Document query) {
        List<Document> documentList = findList(collectionName, query);
        return PeachCollectionUtil.isEmpty(documentList) ? null : documentList.get(0);
    }

    @Override
    public Document findOneAndDelete(String collectionName, Document query) {
        return getDBCollection(collectionName).findOneAndDelete(query);
    }

    @Override
    public Document findOneAndUpdate(String collectionName, Document query, Document update) {
        return getDBCollection(collectionName).findOneAndUpdate(query, update);
    }

    @Override
    public Document findOneAndReplace(String collectionName, Document query, Document replacement) {
        return getDBCollection(collectionName).findOneAndReplace(query, replacement);
    }

    @Override
    public PageInfo<T> findPageList(String collectionName, Document query, Document sort, Document projection, PageInfo<T> pageInfo, Class<T> clz) {
        try {
            MongoCollection<Document> collection = getDBCollection(collectionName);
    
            int total = query == null ? (int) collection.estimatedDocumentCount() : (int) collection.countDocuments(query);
            pageInfo.setTotal(total);
            List<T> resultList = new ArrayList<>();
            if (total == 0) {
                pageInfo.setList(resultList);
                return pageInfo;
            }

            int pages = (int) Math.ceil((double) total / pageInfo.getPageSize());

            sort = sort == null ? new Document("createTime", -1) : sort;
            pageInfo.setPages(pages);

            // 处理分页查询
            assert query != null;
            try (MongoCursor<Document> cursor = collection.find(query)
                    .sort(sort)
                    .projection(projection)
                    .batchSize(Math.max(100, pageInfo.getPageSize()))  // 限制批量大小，优化性能
                    .skip((pageInfo.getPageNum() - 1) * pageInfo.getPageSize())
                    .limit(pageInfo.getPageSize())
                    .iterator()) {  // 使用 try-with-resources 确保游标关闭
                
                while (cursor.hasNext()) {
                    Document document = cursor.next();
                    document.put("_id", document.get("id", ""));
                    document.put("createTime", DateUtil.formatDate(document.getDate("createTime")));
                    document.put("updateTime", DateUtil.formatDate(document.getDate("updateTime")));
                    T t = JSON.parseObject(document.toJson(), clz);
                    resultList.add(t);
                }
                pageInfo.setList(resultList);
            }
        } catch (Exception e) {
            log.error("findPageList error params,collectionName is:[{}],query:[{}],sort:[{}],projection:[{}],pageInfo:[{}]",
                    collectionName,query,sort, projection,pageInfo);
            log.error("findPageList error"+e.getMessage(),e);
            pageInfo.setList(new ArrayList<>());
        }
        return pageInfo;

    }

    @Override
    public List<T> findList(String collectionName, Document query, Document sort, Document projection, Class<T> clz) {
        List<T> resultList = Lists.newArrayList();
        try {
            MongoCollection<Document> collection = getDBCollection(collectionName);
            sort = sort == null ? new Document("createTime", -1) : sort;

            // 处理分页查询
            try (MongoCursor<Document> cursor = collection.find(query).sort(sort).projection(projection).iterator()) {  // 使用 try-with-resources 确保游标关闭

                while (cursor.hasNext()) {
                    Document document = cursor.next();
                    document.put("_id", document.get("id", ""));
                    document.put("createTime", DateUtil.formatDate(document.getDate("createTime")));
                    document.put("updateTime", DateUtil.formatDate(document.getDate("updateTime")));
                    T t = JSON.parseObject(document.toJson(), clz);
                    resultList.add(t);
                }
                return resultList;
            }
        } catch (Exception e) {
            log.error("findPageList error params,collectionName is:[{}],query:[{}],sort:[{}],projection:[{}]", collectionName,query,sort, projection);
            log.error("findPageList error"+e.getMessage(),e);
            return Lists.newArrayList();
        }
    }


    @Override
    public List<Document> findList(String collectionName, Document query, Document sort, Document projection) {
        List<Document> resultList = Lists.newArrayList();
        try {
            MongoCollection<Document> collection = getDBCollection(collectionName);
            sort = sort == null ? new Document("createTime", -1) : sort;
           
            try (MongoCursor<Document> cursor = collection.find(query)
                    .sort(sort)
                    .projection(projection)
                    .iterator()) {  // 使用 try-with-resources 确保游标关闭

                return improveDocuments(resultList, cursor);
            }
        } catch (Exception e) {
            log.error("findPageList error params,collectionName is:[{}],query:[{}],sort:[{}],projection:[{}]", collectionName,query,sort, projection);
            log.error("findPageList error"+e.getMessage(),e);
            return Lists.newArrayList();
        }
    }


    @Override
    public long replaceOne(String collectionName, Document query, Document replacement) {
        long modifiedCount;
        try {
            UpdateResult updateResult = getDBCollection(collectionName).replaceOne(query, replacement);
            modifiedCount = updateResult.getModifiedCount();
        } catch (Exception e) {
            log.error("replaceOne error,collectionName is:[{}],document is :[{}],replacement is:[{}]",collectionName,query,replacement);
            modifiedCount = -1;
        }
        return modifiedCount;
    }

    @Override
    public boolean insert(String collectionName, Document document) {
        try {
            getDBCollection(collectionName).insertOne(document);
        }catch (Exception e){
            log.error("insert error,collectionName is:[{}],document is :[{}]",collectionName,document);
            return false;
        }
        return true;
    }

    @Override
    public long updateOne(String collectionName, Document query, Document document) {
        long modifiedCount;
        try {
            UpdateResult updateResult = getDBCollection(collectionName).updateOne(query, document);
            modifiedCount = updateResult.getModifiedCount();
        }catch(Exception ex){
            modifiedCount = -1L;
            log.error("updateOne error,collectionName is:[{}],query is:[{}],document is :[{}]",collectionName,query,document);
        }
        return modifiedCount;
    }

    @Override
    public long updateMany(String collectionName, Document query, Document document) {
        long modifiedCount;
        try {
            UpdateResult updateResult = getDBCollection(collectionName).updateMany(query, document);
            modifiedCount = updateResult.getModifiedCount();
        }catch(Exception ex){
            modifiedCount = -1L;
            log.error("updateMany error,collectionName is:[{}],query is:[{}],document is :[{}]",collectionName,query,document);
        }
        return modifiedCount;
    }

    @Override
    public long deleteOne(String collectionName, Document query) {
        long deletedCount;
        try {
            DeleteResult deleteResult = getDBCollection(collectionName).deleteOne(query);
            deletedCount = deleteResult.getDeletedCount();
        }catch(Exception ex){
            log.error("deleteOne error,collectionName is:[{}],query is :[{}]",collectionName,query);
            deletedCount = -1L;
        }
        return deletedCount;
    }

    @Override
    public long deleteMany(String collectionName, Document query) {
        long deletedCount;
        try {
            DeleteResult deleteResult = getDBCollection(collectionName).deleteMany(query);
            deletedCount = deleteResult.getDeletedCount();
        }catch(Exception ex){
            log.error("deleteOne error,collectionName is:[{}],query is :[{}]",collectionName,query);
            deletedCount = -1;
        }
        return deletedCount;
    }

    @Override
    public List<Document> findList(String collectionName, Document query, Document projection) {
        return findList(collectionName, query, projection, 0, 0);
    }

    @Override
    public List<Document> findList(String collectionName, Document query, Document projection, int skip, int limit) {
        List<Document> resultList = Lists.newArrayList();
        try {
            MongoCollection<Document> collection = getDBCollection(collectionName);
            try (MongoCursor<Document> cursor = collection.find(query)
                    .projection(projection)
                    .skip(skip)
                    .limit(limit)
                    .iterator()) {  // 使用 try-with-resources 确保游标关闭
                return improveDocuments(resultList, cursor);
            }
        } catch (Exception e) {
            log.error("findPageList error params,collectionName is:[{}],query:[{}],sort:[{}],skip:[{}],limit:[{}]", collectionName,query, projection,skip,limit);
            log.error("findPageList error"+e.getMessage(),e);
            return Lists.newArrayList();
        }
    }


    @Override
    public long updateBatchId(String collectionName, List queryArgs, Document update) {
        long modifiedCount;
        try {
            Document query = new Document().append("_id", new BasicDBObject().append("$in", queryArgs.toArray()));
            UpdateResult updateResult = getDBCollection(collectionName).updateMany(query, update);
            modifiedCount = updateResult.getModifiedCount();
        } catch (Exception e) {
            modifiedCount = -1;
            log.error("updateBatchId error,collectionName is:[{}],queryArgs is :[{}],update is:[{}]",collectionName,queryArgs,update);
        }
        return modifiedCount;
    }

    @Override
    public boolean insertMany(String collectionName, List documentLst) {
        try {
            getDBCollection(collectionName).insertMany(documentLst);
        }catch (Exception ex){
            log.error("insertMany error,collectionName is:[{}],documentLst is :[{}]",collectionName,documentLst);
            return false;
        }
        return true;
    }

    /**
     * 完善Document
     * @param resultList 返回对象
     * @param cursor     游标
     * @return
     */
    private List<Document> improveDocuments(List<Document> resultList, MongoCursor<Document> cursor) {
        while (cursor.hasNext()) {
            Document document = cursor.next();
            document.put("_id", document.get("id", ""));
            document.put("createTime", DateUtil.formatDate(document.getDate("createTime")));
            document.put("updateTime", DateUtil.formatDate(document.getDate("updateTime")));
            resultList.add(document);
        }
        return resultList;
    }

}
