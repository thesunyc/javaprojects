package indi.sunyc.mongo.dao;

import indi.sunyc.base.db.model.PageRequest;
import indi.sunyc.base.db.model.PageResult;
import indi.sunyc.base.db.model.SortRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by ChamIt-001 on 2017/5/9.
 */
public interface IBaseDao {

    public static enum DB_KEY {
        ID("id");
        private String value;
        DB_KEY(String value){this.value = value;}
        public String getValue(){return this.value;}
    }

    public <T> void save(T t);

    public <T> void insertAll(List<T> objs);

    public <T> T get(String id);

    public <T> T get(String key, Object value);

    public <T> T get(Map<String, Object> paramMap);

    public Boolean exist(String key, Object value);

    public Boolean existExceptId(String id, String key, Object value);

    public <T> T get(String key, Object value, SortRequest sortRequest);

    public <T> T get(Map<String, Object> paramMap, SortRequest sortRequest);

    public <T> void update(T t);

    public void update(String id, String key, Object value);

    public <T> List<T> getList(String key, Object value);

    public <T> List<T> getList(Map<String, Object> paramMap);

    public <T> List<T> getList(String key, Object value, SortRequest sortRequest);

    public <T> List<T> getList(Map<String, Object> paramMap, SortRequest sortRequest);

    public Long getSize(Map<String, Object> paramMap);

    public <T> PageResult getPageResult(Map<String, Object> paramMap, PageRequest pageRequest);

    public <T> PageResult getPageResult(Map<String, Object> paramMap, PageRequest pageRequest, SortRequest sortRequest);

    public <T> List<T> getPageList(Map<String, Object> paramMap, PageRequest pageRequest, SortRequest sortRequest);

    public void delete(String id);

    public void delete(String key, Object value);

    public void delete(Map<String, Object> paramMap);

    public void inc(Map<String, Object> paramMap, String incField, Integer incValue);

    public void inc(Map<String, Object> paramMap, String incField, BigDecimal incValue);

}
