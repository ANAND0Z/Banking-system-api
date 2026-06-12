package org.bank.bankingsystem.dao;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenericDao {

    private final SessionFactory sessionFactory;

    public <T> T create(T entity) {

        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {

            tx = session.beginTransaction();

            session.persist(entity);

            tx.commit();
            return entity;

        } catch (Exception e) {

            try {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception rollbackEx) {
                System.out.println(rollbackEx);
            }

            throw e;
        }
    }

    public <T> T update(T entity) {

        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {

            tx = session.beginTransaction();

            session.merge(entity);

            tx.commit();
            return entity;

        } catch (Exception e) {

            try {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception rollbackEx) {
                System.out.println(rollbackEx);
            }

            throw e;
        }
    }

    public <T> T findById(Class<T> clazz, Object id) {

        try (Session session = sessionFactory.openSession()) {
            return session.get(clazz, (java.io.Serializable) id);
        }
    }

    public <T> List<T> findAll(Class<T> clazz) {

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "from " + clazz.getName(), clazz
            ).list();

        }
    }

    public <T> void delete(Class<T> clazz, Object id) {

        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            T entity = session.get(clazz, (java.io.Serializable) id);
            if (entity != null) {
                session.remove(entity);
            }

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;

        }
    }
}

