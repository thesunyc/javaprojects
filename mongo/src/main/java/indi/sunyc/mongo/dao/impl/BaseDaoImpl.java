package indi.sunyc.mongo.dao.impl;

import indi.sunyc.base.db.model.PageRequest;
import indi.sunyc.base.db.model.PageResult;
import indi.sunyc.base.db.model.SortRequest;
import indi.sunyc.base.util.DateUtil;
import indi.sunyc.base.util.StringUtil;
import indi.sunyc.mongo.dao.IBaseDao;
import indi.sunyc.mongo.entity.BaseEntity;
import indi.sunyc.mongo.model.UpdateUser;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ChamIt-001 on 2017/5/9.
 */
public abstract class BaseDaoImpl implements IBaseDao {

    public abstract Class getEntityClass();

    /**
     * 需重写，更新时自动更新操作人id和名称
     * @return
     */
    public UpdateUser getUpdateUser(){
        return null;
    }

    @Autowired
    protected MongoOperations mongoOperations;

    @Override
    public void save(Object o) {
        setAttr(o,true);
        mongoOperations.save(o);
    }

    @Override
    public <T> void insertAll(List<T> objs){
        for(Object obj:objs){
            setAttr(obj,true);
        }
        mongoOperations.insertAll(objs);
    }

    private void setAttr(Object o,Boolean isCreate) {
        if(o instanceof BaseEntity){
            BaseEntity baseEntity = (BaseEntity)o;
            UpdateUser updateUser = getUpdateUser();
            if(updateUser != null) {
                if(isCreate) {
                    baseEntity.setCreateUserId(updateUser.getId());
                    baseEntity.setCreateUserName(updateUser.getName());
                }
                baseEntity.setUpdateUserId(updateUser.getId());
                baseEntity.setUpdateUserName(updateUser.getName());
            }
            if(isCreate) {
                baseEntity.setCreateDate(DateUtil.newDateTimeString());
            }
            baseEntity.setUpdateDate(DateUtil.newDateTimeString());
        }
    }

    @Override
    public <T> void update(T t) {
        try {
            setAttr(t,false);
            Update update = new Update();
            Map<String,Object> beanMap = PropertyUtils.describe(t);
            Field[] fields = t.getClass().getDeclaredFields();
            for(Field field : fields){
                if(!field.isAnnotationPresent(Transient.class) && !field.getName().equals(DB_KEY.ID.getValue()) && beanMap.get(field.getName()) != null) {
                    update.set(field.getName(), beanMap.get(field.getName()));
                }
            }
            Query query = new Query(Criteria.where(DB_KEY.ID.getValue()).is(beanMap.get(DB_KEY.ID.getValue())));
            mongoOperations.updateFirst(query,update,getEntityClass());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(String id, String key, Object value) {
        mongoOperations.updateFirst(new Query(Criteria.where(DB_KEY.ID.getValue()).is(id)), new Update().set(key,value),getEntityClass());
    }

    @Override
    public <T> T get(String id) {
        return (T) mongoOperations.findOne(new Query(Criteria.where(DB_KEY.ID.getValue()).is(id)),getEntityClass());
    }

    @Override
    public <T> T get(String key, Object value) {
        return (T) mongoOperations.findOne(new Query(Criteria.where(key).is(value)),getEntityClass());
    }

    @Override
    public <T> T get(Map<String, Object> paramMap) {
        return (T) mongoOperations.findOne(new Query(CriteriaCombiner.combineParam(paramMap)),getEntityClass());
    }

    @Override
    public <T> List<T> getList(String key, Object value) {
        return (List<T>) mongoOperations.find(new Query(Criteria.where(key).is(value)),getEntityClass());
    }

    @Override
    public <T> List<T> getList(Map<String, Object> paramMap) {
        return (List<T>) mongoOperations.find(new Query(CriteriaCombiner.combineParam(paramMap)),getEntityClass());
    }

    @Override
    public Long getSize(Map<String, Object> paramMap) {
        return mongoOperations.count(new Query(CriteriaCombiner.combineParam(paramMap)),getEntityClass());
    }

    @Override
    public <T> PageResult getPageResult(Map<String,Object> paramMap, PageRequest pageRequest) {
    	return getPageResult(paramMap,pageRequest,null);
    }

    @Override
    public <T> PageResult getPageResult(Map<String,Object> paramMap, PageRequest pageRequest, SortRequest sortRequest) {
        return new PageResult(pageRequest,getSize(paramMap),getPageList(paramMap,pageRequest,sortRequest));
    }

    @Override
    public <T> List<T> getPageList(Map<String,Object> paramMap, PageRequest pageRequest, SortRequest sortRequest){
    	Sort sort = getSort(sortRequest);
	    Integer skip = (pageRequest.getPageIndex()) * pageRequest.getPageSize();
	    Query query = new Query(CriteriaCombiner.combineParam(paramMap)).skip(skip).limit(pageRequest.getPageSize());
	    if(sort!=null){
	        query.with(sort);
	    }
	    return mongoOperations.find(query,getEntityClass());
	}

    private Sort getSort(SortRequest sortRequest){
        Sort sort = null;
        if(sortRequest!=null && !StringUtil.isEmpty(sortRequest.getSortKey()) && !StringUtil.isEmpty(sortRequest.getSortDirection())){
            sort = new Sort(new Sort.Order("asc".equals(sortRequest.getSortDirection())?Sort.Direction.ASC:Sort.Direction.DESC,sortRequest.getSortKey()));
        }
        return sort;
    }

	@Override
    public Boolean exist(String key, Object value) {
        return mongoOperations.exists(new Query(Criteria.where(key).is(value)),getEntityClass());
    }

    @Override
    public Boolean existExceptId(String id,String key,Object value) {
        return mongoOperations.exists(new Query(Criteria.where(key).is(value).and(DB_KEY.ID.getValue()).ne(id)),getEntityClass());
    }

    @Override
    public <T> T get(String key, Object value,SortRequest sortRequest) {
        return (T) mongoOperations.findOne(new Query(Criteria.where(key).is(value)).with(getSort(sortRequest)).limit(1), getEntityClass());
    }

    @Override
    public <T> T get(Map<String,Object> paramMap,SortRequest sortRequest) {
        return (T) mongoOperations.findOne(new Query(CriteriaCombiner.combineParam(paramMap)).with(getSort(sortRequest)).limit(1),getEntityClass());
    }

    @Override
    public <T> List<T> getList(String key, Object value,SortRequest sortRequest) {
        return (List<T>) mongoOperations.find(new Query(Criteria.where(key).is(value)).with(getSort(sortRequest)),getEntityClass());
    }

    @Override
    public <T> List<T> getList(Map<String, Object> paramMap,SortRequest sortRequest) {
        return (List<T>) mongoOperations.find(new Query(CriteriaCombiner.combineParam(paramMap)).with(getSort(sortRequest)),getEntityClass());
    }

    @Override
    public void delete(String id) {
        mongoOperations.remove(new Query(Criteria.where(DB_KEY.ID.getValue()).is(id)),getEntityClass());
    }

    @Override
    public void delete(String key, Object value) {
        mongoOperations.remove(new Query(Criteria.where(key).is(value)),getEntityClass());
    }

    @Override
    public void delete(Map<String, Object> paramMap) {
        mongoOperations.remove(new Query(CriteriaCombiner.combineParam(paramMap)),getEntityClass());
    }

    @Override
    public void inc(Map<String, Object> paramMap, String incField, Integer incValue) {
        mongoOperations.updateFirst(new Query(CriteriaCombiner.combineParam(paramMap)),new Update().inc(incField,incValue),getEntityClass());
    }

    @Override
    public void inc(Map<String, Object> paramMap, String incField, BigDecimal incValue) {
        mongoOperations.updateFirst(new Query(CriteriaCombiner.combineParam(paramMap)),new Update().inc(incField,incValue),getEntityClass());
    }
    
}

class CriteriaCombiner {
    enum DB_OPERATOR_MATCHES {
        OR_SEPARATOR("||"),
        OR("__or"),
        IN("__in"),
        NIN("__nin"),
        GT("__gt"),
        GTE("__gte"),
        LT("__lt"),
        LTE("__lte"),
        BLK("__blk"),
        RLK("__rlk"),
        LLK("__llk"),
        BET("__bet"),
        NE("__ne"),
        IGNORE("__ign");
        private String value;
        DB_OPERATOR_MATCHES(String value){this.value = value;}
        public String getValue(){return this.value;}
    }

    public static Criteria combineParam(Map<String,Object> paramMap) {
        Integer index = 0;
        Criteria criteria = new Criteria();
        List<Criteria> criteriaList = new ArrayList<Criteria>();
        for(Map.Entry<String,Object> entry: paramMap.entrySet()) {
            if(entry.getKey().indexOf(DB_OPERATOR_MATCHES.OR.getValue()) != -1) {//或
                String fieldName = entry.getKey().substring(0,entry.getKey().indexOf(DB_OPERATOR_MATCHES.OR.getValue()));
                List<Criteria> criterias = new ArrayList<Criteria>();
                List<Object> orList = (List<Object>)entry.getValue();
                for(Object orValue : orList) {
                    criterias.add(Criteria.where(fieldName).is(orValue));
                }
                Criteria[] tmpArr = new Criteria[criterias.size()];
                criteriaList.add(new Criteria().orOperator(criterias.toArray(tmpArr)));
            } else if(entry.getKey().indexOf(DB_OPERATOR_MATCHES.IN.getValue()) != -1) {//包括
                String fieldName = entry.getKey().substring(0,entry.getKey().indexOf(DB_OPERATOR_MATCHES.IN.getValue()));
                List<Object> rangeList = (List<Object>) entry.getValue();
                criteriaList.add(new Criteria().where(fieldName).in(rangeList));
            } else if(entry.getKey().indexOf(DB_OPERATOR_MATCHES.NIN.getValue()) != -1) {//不包括
                String fieldName = entry.getKey().substring(0,entry.getKey().indexOf(DB_OPERATOR_MATCHES.NIN.getValue()));
                List<Object> rangeList = (List<Object>) entry.getValue();
                criteriaList.add(new Criteria().where(fieldName).nin(rangeList));
            } else if(entry.getKey().indexOf(DB_OPERATOR_MATCHES.OR_SEPARATOR.getValue()) != -1) {//或分隔符
                String[] querys = entry.getKey().split("\\|\\|");
                List<Criteria> criterias = new ArrayList<Criteria>();
                for(String query : querys) {
                    Map<String,Object> subQueryMap = new HashMap<String,Object>();
                    subQueryMap.put(query,entry.getValue());
                    criterias.add(combineParam(subQueryMap));
                }
                Criteria[] tmpArr = new Criteria[criterias.size()];
                criteriaList.add(new Criteria().orOperator(criterias.toArray(tmpArr)));
            } else if(entry.getKey().indexOf(DB_OPERATOR_MATCHES.GTE.getValue()) != -1) {//大于等于
                String fieldName = entry.getKey().substring(0,entry.getKey().indexOf(DB_OPERATOR_MATCHES.GTE.getValue()));
                if(paramMap.containsKey(StringUtil.concat(fieldName,DB_OPERATOR_MATCHES.LTE.getValue()))){
                    //当存在同字段小于等于时组合
                    criteriaList.add(new Criteria().where(fieldName).gte(entry.getValue()).lte(paramMap.get(StringUtil.concat(fieldName,DB_OPERATOR_MATCHES.LTE.getValue()))));
                } else {
                    criteriaList.add(new Criteria().where(fieldName).gte(entry.getValue()));
                }
            } else if(entry.getKey().indexOf(DB_OPERATOR_MATCHES.LTE.getValue()) != -1) {//小于等于
                String fieldName = entry.getKey().substring(0,entry.getKey().indexOf(DB_OPERATOR_MATCHES.LTE.getValue()));
                if(paramMap.containsKey(StringUtil.concat(fieldName,DB_OPERATOR_MATCHES.GTE.getValue()))){
                    continue;
                }
                criteriaList.add(new Criteria().where(fieldName).lte(entry.getValue()));
            } else if(entry.getKey().indexOf(DB_OPERATOR_MATCHES.GT.getValue()) != -1) {//大于
                String fieldName = entry.getKey().substring(0,entry.getKey().indexOf(DB_OPERATOR_MATCHES.GT.getValue()));
                if(paramMap.containsKey(StringUtil.concat(fieldName,DB_OPERATOR_MATCHES.LT.getValue()))){
                    //当存在同字段小于等于时组合
                    criteriaList.add(new Criteria().where(fieldName).gt(entry.getValue()).lt(paramMap.get(StringUtil.concat(fieldName,DB_OPERATOR_MATCHES.LTE.getValue()))));
                } else {
                    criteriaList.add(new Criteria().where(fieldName).gt(entry.getValue()));
                }
            } else if(entry.getKey().indexOf(DB_OPERATOR_MATCHES.LT.getValue()) != -1) {//小于
                String fieldName = entry.getKey().substring(0,entry.getKey().indexOf(DB_OPERATOR_MATCHES.LT.getValue()));
                if(paramMap.containsKey(StringUtil.concat(fieldName,DB_OPERATOR_MATCHES.GT.getValue()))){
                    continue;
                }
                criteriaList.add(new Criteria().where(fieldName).lt(entry.getValue()));
            } else if(entry.getKey().indexOf(DB_OPERATOR_MATCHES.BET.getValue()) != -1) {//范围
                String fieldName = entry.getKey().substring(0,entry.getKey().indexOf(DB_OPERATOR_MATCHES.BET.getValue()));
                List<Object> rangeList = (List<Object>) entry.getValue();
                criteriaList.add(new Criteria().where(fieldName).gte(rangeList.get(0)).lte(rangeList.get(1)));
            } else if(entry.getKey().indexOf(DB_OPERATOR_MATCHES.BLK.getValue()) != -1) {//模糊匹配
                String fieldName = entry.getKey().substring(0,entry.getKey().indexOf(DB_OPERATOR_MATCHES.BLK.getValue()));
                criteriaList.add(new Criteria().where(fieldName).regex(entry.getValue().toString()));
            } else if(entry.getKey().indexOf(DB_OPERATOR_MATCHES.RLK.getValue()) != -1) {//右模糊匹配
                String fieldName = entry.getKey().substring(0,entry.getKey().indexOf(DB_OPERATOR_MATCHES.RLK.getValue()));
                criteriaList.add(new Criteria().where(fieldName).regex(StringUtil.concat("^",entry.getValue().toString())));
            } else if(entry.getKey().indexOf(DB_OPERATOR_MATCHES.LLK.getValue()) != -1) {//左模糊匹配
                String fieldName = entry.getKey().substring(0,entry.getKey().indexOf(DB_OPERATOR_MATCHES.LLK.getValue()));
                criteriaList.add(new Criteria().where(fieldName).regex(StringUtil.concat(entry.getValue().toString(),"$")));
            } else if(entry.getKey().indexOf(DB_OPERATOR_MATCHES.NE.getValue()) != -1) {//不等于
                String fieldName = entry.getKey().substring(0,entry.getKey().indexOf(DB_OPERATOR_MATCHES.NE.getValue()));
                criteriaList.add(new Criteria().where(fieldName).ne(entry.getValue()));
            } else if(entry.getKey().indexOf(DB_OPERATOR_MATCHES.IGNORE.getValue()) != -1) {//跳过
                continue;
            } else {
                criteriaList.add(new Criteria().where(entry.getKey()).where(entry.getKey()).is(entry.getValue()));
            }
            index++;
        }
        if(criteriaList.size()>0){
            criteria.andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
        }
        return criteria;
    }
}
