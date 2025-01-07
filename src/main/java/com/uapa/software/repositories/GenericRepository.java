package com.uapa.software.repositories;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GenericRepository<T> implements IRepository<T> {

    private static final Logger logger = LoggerFactory.getLogger(GenericRepository.class);

    private final Session session;

    protected GenericRepository(Session session) {
        this.session = session;
    }

    @Override
    public T save(T entity) {
        if (entity == null) {
            logger.error("Entity cannot be null");
            return null;
        }
        return executeTransaction(session -> {
            session.persist(entity);
            return entity;
        });
    }

    @Override
    public boolean update(T entity) {
        if (entity == null) {
            logger.error("Entity cannot be null");
            return false;
        }
        return executeTransaction(session -> {
            session.merge(entity);
            return true;
        });
    }

    @Override
    public boolean delete(T entity) {
        if (entity == null) {
            logger.error("Entity cannot be null");
            return false;
        }
        return executeTransaction(session -> {
            session.remove(entity);
            return true;
        });
    }

    @Override
    public Optional<T> findById(int id) {
        try {
            T entity = session.get(getEntityType(), id);
            return Optional.ofNullable(entity);
        } catch (Exception ex) {
            logger.error("Error fetching entity by ID: {}", ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    @Override
    public List<T> findAll() {
        try {
            Query<T> query = session.createQuery("FROM " + getEntityType().getSimpleName(), getEntityType());
            return query.list();
        } catch (Exception ex) {
            logger.error("Error fetching entities: {}", ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    public <R> R executeTransaction(TransactionFunction<R> function) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Transaction failed: {}", ex.getMessage(), ex);
            throw new RuntimeException("Transaction failed", ex);
        }
    }

    protected Class<T> getEntityType() {
        throw new UnsupportedOperationException("Implement getEntityType method");
    }

    @FunctionalInterface
    public interface TransactionFunction<R> {
        R apply(Session session) throws Exception;
    }
}
