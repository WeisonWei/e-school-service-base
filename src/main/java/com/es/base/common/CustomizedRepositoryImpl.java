package com.es.base.common;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class CustomizedRepositoryImpl<T extends Object, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements CustomizedRepository<T, ID> {

    private final EntityManager entityManager;

    public CustomizedRepositoryImpl(Class<T> domainClass,
                                         EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    public CustomizedRepositoryImpl(JpaEntityInformation entityInformation,
                                         EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public <S extends T> S save(S entity) {
        return super.save(entity);
    }

    @Override
    public <S extends T> S update(S entity) {
        return entityManager.merge(entity);
    }

    @Override
    public <S extends T> Iterable<S> updateAll(Iterable<S> entities) {
        return entityManager.merge(entities);
    }

    @Override
    public <S extends T> S insert(S entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> insertAll(Iterable<S> entities) {
        entityManager.persist(entities);
        return entities;
    }
}
