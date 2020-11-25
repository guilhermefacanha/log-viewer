package br.com.gfsolucoesti.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.sql.JoinType;

import br.com.gfsolucoesti.entity.BaseEntity;
import br.com.gfsolucoesti.utils.GFUtils;

@SuppressWarnings({ "unchecked", "deprecation" })
public abstract class BaseDao<T extends BaseEntity> implements Serializable {

    private static final long serialVersionUID = 7400219292515786319L;

    @PersistenceContext
    protected EntityManager manager;

    @Inject
    private BeanManager beanManager;

    private Session manager_session;

    protected Class<T> modelClass;

    @Transactional
    public T save(T ent) {
        ent = manager.merge(ent);
        return ent;
    }

    @Transactional
    public void remove(T obj) {
        if (obj.getId() > 0) {
            obj = get(obj.getId());
            manager.remove(obj);
        }
    }

    @Transactional
    public void remove(Object id) {
        manager.createQuery("DELETE FROM " + getEntityName() + " x WHERE x.id=:id").setParameter("id", id)
                .executeUpdate();
    }

    /**
     * Get Object By Id Methods
     */

    public T get(long id) {
        try {
            return (T) manager.find(getEntityClass(), id);
        } catch (NoResultException e) {
            return null;
        }
    }

    public T get(Object id) {
        Class<? extends BaseEntity> classe = getEntityClass();
        try {
            return (T) manager.find(classe, id);
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Get All Records Methods
     */

    public List<T> getAll() {
        return manager.createQuery("from " + getEntityName()).getResultList();
    }

    public List<T> getAll(Map<String, String> joins, List<Criterion> restrictions, String orderBy) {
        Class<? extends BaseEntity> classe = getEntityClass();

        Criteria crit = getSession().createCriteria(classe);
        for (Entry<String, String> j : joins.entrySet()) {
            crit.createAlias(j.getKey(), j.getValue(), JoinType.INNER_JOIN);
        }

        restrictions.forEach(crit::add);

        crit.addOrder(Order.asc(orderBy));

        return crit.list();
    }

    public List<T> getAll(Map<String, String> joins) {
        Class<? extends BaseEntity> classe = getEntityClass();

        Criteria crit = getSession().createCriteria(classe);
        for (Entry<String, String> j : joins.entrySet()) {
            crit.createAlias(j.getKey(), j.getValue(), JoinType.INNER_JOIN);
        }
        crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return crit.list();
    }

    public List<T> getAll(String orderBy) {
        String classe = getEntityClass().getSimpleName();
        return manager.createQuery("SELECT x FROM " + classe + " x ORDER BY " + orderBy).getResultList();
    }

    public List<T> getAll(Map<String, String> joins, String orderBy) {
        Class<? extends BaseEntity> classe = getEntityClass();

        Criteria crit = getSession().createCriteria(classe);
        for (Entry<String, String> j : joins.entrySet()) {
            crit.createAlias(j.getKey(), j.getValue(), JoinType.INNER_JOIN);
        }

        crit.addOrder(Order.asc(orderBy));

        return crit.list();
    }

    /**
     * Get All Records with Pagination Methods
     */

    public List<T> getAllPaged(String orderBy, String dir, int beginWith, int limit) {

        Criteria crit = getSession().createCriteria(getEntityClass());
        if (dir.equals("asc"))
            crit.addOrder(Order.asc(orderBy));
        else
            crit.addOrder(Order.desc(orderBy));
        crit.setFirstResult(beginWith);
        crit.setMaxResults(limit);
        return crit.list();
    }

    public List<T> getAllPaged(String orderBy, String dir, int beginWith, int limit, List<Criterion> restrictions) {

        Criteria crit = getSession().createCriteria(getEntityClass());
        if (dir.equals("asc"))
            crit.addOrder(Order.asc(orderBy));
        else
            crit.addOrder(Order.desc(orderBy));

        restrictions.forEach(crit::add);

        crit.setFirstResult(beginWith);
        crit.setMaxResults(limit);
        return crit.list();

    }

    /**
     * Get Records Count Methods
     */

    public long getTotalRecords() {

        Criteria crit = getSession().createCriteria(getEntityClass());
        crit.setProjection(Projections.count("id"));

        return (Long) crit.uniqueResult();
    }

    public long getTotalRecords(List<Criterion> restrictions) {

        Criteria crit = getSession().createCriteria(getEntityClass());
        crit.setProjection(Projections.count("id"));

        restrictions.forEach(crit::add);

        return (Long) crit.uniqueResult();
    }

    /**
     * Get List By Attributes Method
     */

    public List<T> getListBy(Map<String, Object> attrs) {
        Class<? extends BaseEntity> classe = getEntityClass();

        Criteria crit = getSession().createCriteria(classe);
        for (Entry<String, Object> a : attrs.entrySet()) {
            crit.add(Restrictions.eq(a.getKey(), a.getValue()));
        }

        return crit.list();
    }

    public List<T> getListBy(Map<String, Object> attrs, String orderBy) {
        Class<? extends BaseEntity> classe = getEntityClass();

        Criteria crit = getSession().createCriteria(classe);
        for (Entry<String, Object> a : attrs.entrySet()) {
            crit.add(Restrictions.eq(a.getKey(), a.getValue()));
        }

        crit.addOrder(Order.asc(orderBy));

        return crit.list();
    }

    public List<T> getListBy(Map<String, Object> attrs, List<Order> orderList) {
        Class<? extends BaseEntity> classe = getEntityClass();

        Criteria crit = getSession().createCriteria(classe);
        for (Entry<String, Object> a : attrs.entrySet()) {
            crit.add(Restrictions.eq(a.getKey(), a.getValue()));
        }

        orderList.forEach(crit::addOrder);

        return crit.list();
    }

    public List<T> getListBy(List<Criterion> restriction) {
        Class<? extends BaseEntity> classe = getEntityClass();

        Criteria crit = getSession().createCriteria(classe);
        restriction.forEach(crit::add);

        return crit.list();
    }

    public List<T> getListBy(List<Criterion> restrictions, String orderBy) {
        Class<? extends BaseEntity> classe = getEntityClass();

        Criteria crit = getSession().createCriteria(classe);
        for (Criterion r : restrictions) {
            crit.add(r);
        }

        crit.addOrder(Order.asc(orderBy));

        return crit.list();
    }

    public List<T> getListBy(List<Criterion> restrictions, List<String> joins) {
        Class<? extends BaseEntity> classe = getEntityClass();

        Criteria crit = getSession().createCriteria(classe);
        for (String join : joins)
            crit.createAlias(join, join, JoinType.INNER_JOIN);

        Criterion[] restricoesArray = new Criterion[restrictions.size()];
        restricoesArray = restrictions.toArray(restricoesArray);

        crit.add(Restrictions.or(restricoesArray));

        return crit.list();
    }

    public List<T> getListBy(List<Criterion> restrictions, List<String> joins, String orderBy) {
        Class<? extends BaseEntity> classe = getEntityClass();

        Criteria crit = getSession().createCriteria(classe);
        for (String join : joins)
            crit.createAlias(join, join, JoinType.INNER_JOIN);

        Criterion[] restricoesArray = new Criterion[restrictions.size()];
        restricoesArray = restrictions.toArray(restricoesArray);

        crit.add(Restrictions.or(restricoesArray));

        crit.addOrder(Order.asc(orderBy));

        return crit.list();
    }

    public List<T> getListBy(String attr, Object value) {
        return manager.createQuery("SELECT x FROM " + getEntityName() + " x WHERE x." + attr + " = :valor")
                .setParameter("valor", value).getResultList();
    }

    public List<T> getListBy(String attr, Object value, String orderBy) {
        return manager.createQuery("SELECT x FROM " + getEntityName() + " x WHERE x." + attr + " = :valor ORDER BY x." + orderBy)
                .setParameter("valor", value).getResultList();
    }

    /**
     * Get Single Object By Attributes Methods
     */
    public T getObjectBy(String attr, Object value) {
        String classe = getEntityName();
        try {
            return (T) manager.createQuery("SELECT x FROM " + classe + " x WHERE x." + attr + " = :valor")
                    .setParameter("valor", value).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public T getObjectBy(String attr, String value, String orderBy) {
        String classe = getEntityName();
        try {
            return (T) manager.createQuery(
                    "SELECT x FROM " + classe + " x WHERE x." + attr + " = :valor ORDER BY " + orderBy)
                    .setParameter("valor", value).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public T getFirst(String attr, Object value, String orderBy, boolean asc) {
        if (!asc && orderBy.contains(",")) {
            String res = "";
            String[] split = orderBy.split(",");
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                if (i == split.length - 1)
                    res += s;
                else
                    res += s + " desc,";
            }
            orderBy = res;
        }
        String order = asc ? "" : " desc";
        @SuppressWarnings("rawtypes")
        List lista = manager.createQuery("SELECT x FROM " + getEntityName() + " x WHERE x." + attr
                + " = :valor ORDER BY " + orderBy + order).setParameter("valor", value).setMaxResults(1)
                .getResultList();
        if (GFUtils.isValidList(lista))
            return (T) lista.get(0);

        return null;
    }

    /**
     * Verify Exists Object By Methods
     */
    public boolean objectExistsBy(String attr, Object value) {
        try {
            Criteria crit = createCriteria();

            crit.add(Restrictions.eq(attr, value));

            Object o = crit.uniqueResult();

            return o != null;
        } catch (HibernateException e) {
            return true;
        }
    }

    public boolean objectExistsBy(Map<String, Object> attrs) {
        try {
            Criteria crit = createCriteria();

            for (Entry<String, Object> a : attrs.entrySet()) {
                crit.add(Restrictions.eq(a.getKey(), a.getValue()));
            }

            Object o = crit.uniqueResult();

            return o == null ? false : true;
        } catch (HibernateException e) {
            return false;
        }
    }

    public boolean objectExistsByIgnoreCase(Long id, String attr, Object value) {
        try {
            Criteria crit = createCriteria();

            if (id != null && id > 0) {
                crit.add(Restrictions.ne("id", id));
            }

            crit.add(Restrictions.eq(attr, value).ignoreCase());

            Object o = crit.uniqueResult();

            return o != null;
        } catch (HibernateException e) {
            return true;
        }
    }

    /**
     * Helper Methods
     */
    
    public void updateSequence(String sequence) {
        String q = "SELECT setval('"+sequence+"', (select max(id) from loc.city c), true)";
        manager.createNativeQuery(q).executeUpdate();
    }
    
    public void testSelect() {
        // Works for H2, MySQL, Microsoft SQL Server, PostgreSQL, SQLite
        String sql = "SELECT 1";

        // Works for Oracle
        // String sql = "SELECT 1 FROM DUAL";
        manager.createNativeQuery(sql).getFirstResult();
    }

    protected Criteria createCriteria() {
        Criteria crit = getSession().createCriteria(getEntityClass());
        return crit;
    }

    protected <C extends BaseEntity> Criteria createCriteria(Class<C> c) {
        Criteria crit = getSession().createCriteria(c);
        return crit;
    }

    @SuppressWarnings("rawtypes")
    protected Session getSession() {
        try {
            return (Session) manager.getDelegate();
        } catch (Exception e) {
        }

        if (manager_session == null && beanManager != null) {
            Context injectionContext = this.beanManager.getContext(ApplicationScoped.class);
            Bean entityManagerBean = this.beanManager.resolve(beanManager.getBeans(EntityManager.class));
            EntityManager entityManager = (EntityManager) injectionContext.get(entityManagerBean);
            this.manager_session = entityManager.unwrap(SessionImplementor.class);
        }

        return manager_session;
    }

    private String getEntityName() {
        return getEntityClass().getSimpleName();
    }

    @SuppressWarnings("rawtypes")
    private Class<T> getEntityClass() {
        if (this.modelClass == null) {
            if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
                this.modelClass = (Class) ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
            } else if (((Class) getClass().getGenericSuperclass())
                    .getGenericSuperclass() instanceof ParameterizedType) {
                this.modelClass = (Class<T>) ((ParameterizedType) ((Class) getClass().getGenericSuperclass())
                        .getGenericSuperclass()).getActualTypeArguments()[0];
            }
        }
        return this.modelClass;
    }

}
