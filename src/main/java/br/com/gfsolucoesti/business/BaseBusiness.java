package br.com.gfsolucoesti.business;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import br.com.gfsolucoesti.dao.BaseDao;
import br.com.gfsolucoesti.entity.BaseEntity;

public abstract class BaseBusiness<T extends BaseEntity> implements Serializable {

    private static final long serialVersionUID = -1887739289518086311L;

    protected abstract BaseDao<T> getDao();

    public T save(T ent) {
        return getDao().save(ent);
    }

    public void remove(T obj) {
        getDao().remove(obj);
    }

    public void remove(Object id) {
        getDao().remove(id);
    }

    /**
     * Get Object By Id Methods
     */

    public T get(long id) {
        return getDao().get(id);
    }

    public T get(Object id) {
        return getDao().get(id);
    }

    /**
     * Get All Records Methods
     */

    public List<T> getAll() {
        return getDao().getAll();
    }

    public List<T> getAll(Map<String, String> joins, List<Criterion> restrictions, String orderBy) {
        return getDao().getAll(joins, restrictions, orderBy);
    }

    public List<T> getAll(Map<String, String> joins) {
        return getDao().getAll(joins);
    }

    public List<T> getAll(String orderBy) {
        return getDao().getAll(orderBy);
    }

    public List<T> getAll(Map<String, String> joins, String orderBy) {
        return getDao().getAll(joins, orderBy);
    }

    /**
     * Get All Records with Pagination Methods
     */

    public List<T> getAllPaged(String orderBy, String dir, int beginWith, int limit) {
        return getDao().getAllPaged(orderBy, dir, beginWith, limit);
    }

    public List<T> getAllPaged(String orderBy, String dir, int beginWith, int limit, List<Criterion> restrictions) {
        return getDao().getAllPaged(orderBy, dir, beginWith, limit, restrictions);
    }

    /**
     * Get Records Count Methods
     */

    public long getTotalRecords() {
        return getDao().getTotalRecords();
    }

    public long getTotalRecords(List<Criterion> restrictions) {
        return getDao().getTotalRecords(restrictions);
    }

    /**
     * Get List By Attributes Method
     */

    public List<T> getListBy(Map<String, Object> attrs) {
        return getDao().getListBy(attrs);
    }

    public List<T> getListBy(Map<String, Object> attrs, String orderBy) {
        return getDao().getListBy(attrs, orderBy);
    }

    public List<T> getListBy(Map<String, Object> attrs, List<Order> orderList) {
        return getDao().getListBy(attrs, orderList);
    }

    public List<T> getListBy(List<Criterion> restriction) {
        return getDao().getListBy(restriction);
    }

    public List<T> getListBy(List<Criterion> restrictions, String orderBy) {
        return getDao().getListBy(restrictions, orderBy);
    }

    public List<T> getListBy(List<Criterion> restrictions, List<String> joins) {
        return getDao().getListBy(restrictions, joins);
    }

    public List<T> getListBy(List<Criterion> restrictions, List<String> joins, String orderBy) {
        return getDao().getListBy(restrictions, joins, orderBy);
    }

    public List<T> getListBy(String attr, Object value) {
        return getDao().getListBy(attr, value);
    }
    
    public List<T> getListBy(String attr, Object value, String orderBy) {
        return getDao().getListBy(attr, value, orderBy);
    }

    /**
     * Get Single Object By Attributes Methods
     */
    public T getObjectBy(String attr, Object value) {
        return getDao().getObjectBy(attr, value);
    }

    public T getObjectBy(String attr, String value, String orderBy) {
        return getDao().getObjectBy(attr, value, orderBy);
    }

    public T getFirst(String attr, Object value, String orderBy, boolean asc) {
        return getDao().getFirst(attr, value, orderBy, asc);
    }

    /**
     * Verify Exists Object By Methods
     */
    public boolean objectExistsBy(String attr, Object value) {
        return getDao().objectExistsBy(attr, value);
    }

    public boolean objectExistsBy(Map<String, Object> attrs) {
        return getDao().objectExistsBy(attrs);
    }

    public boolean objectExistsByIgnoreCase(Long id, String attr, Object value) {
        return getDao().objectExistsByIgnoreCase(id, attr, value);
    }

    /**
     * Helper Methods
     */
    public void testSelect() {
        getDao().testSelect();
    }

}
