package com.simplejcode.commons.db;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.*;

import javax.persistence.criteria.*;
import java.util.List;

@SuppressWarnings("unchecked")
public class HibernateDAO {

    private final SessionFactory sessionFactory;


    public HibernateDAO(String resource, String url) {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Configuration configuration = new Configuration().configure(classLoader.getResource(resource));
        configuration.setProperty("hibernate.connection.url", url);

        sessionFactory = configuration.buildSessionFactory();

        System.out.println(configuration.getNamedSQLQueries().size());

    }


    public Session createSession() {
        return sessionFactory.openSession();
    }


    public <T> List<T> selectWithNulls(String queryName, Object... params) {

        try (Session session = createSession()) {
            NativeQuery query = session.getNamedNativeQuery(queryName);
            for (int i = 0; i < params.length; i += 3) {
                setParameter(query, (String) params[i], params[i + 1], (Type) params[i + 2]);
            }
            return query.list();
        }

    }

    public <T> List<T> select(String queryName, Object... params) {

        try (Session session = createSession()) {
            return getNativeQuery(queryName, session, params).list();
        }

    }

    public <T> T selectOne(String queryName, Object... params) {
        try (Session session = createSession()) {
            return (T) getNativeQuery(queryName, session, params).uniqueResult();
        }
    }

    private NativeQuery getNativeQuery(String queryName, Session session, Object[] params) {
        NativeQuery query = session.getNamedNativeQuery(queryName);
        for (int i = 0; i < params.length; i += 2) {
            setParameter(query, (String) params[i], params[i + 1], null);
        }
        return query;
    }

    private void setParameter(NativeQuery query, String name, Object param, Type nullType) {
        if (param == null) {
            query.setParameter(name, StringType.INSTANCE, nullType);
        }
        query.setParameter(name, param);
    }


    public <T> T insert(T entity) {

        try (Session session = createSession()) {
            Transaction t = session.beginTransaction();
            session.save(entity);
            t.commit();
            return entity;
        }

    }

    public <T> T update(T entity) {

        try (Session session = createSession()) {
            Transaction t = session.beginTransaction();
            session.update(entity);
            t.commit();
            return entity;
        }

    }

    public <T> T select(Class<T> clazz, Object key) {

        try (Session session = createSession()) {
            return session.find(clazz, key);
        }

    }

    public <T> List<T> select(Class<T> clazz) {

        try (Session session = createSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);
            query.select(query.from(clazz));
            return session.createQuery(query).list();
        }

    }

}

