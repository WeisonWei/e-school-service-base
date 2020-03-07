package com.es.base.common;


import java.io.Serializable;


public interface CustomizedRepository<T extends Object, ID extends Serializable> {

    <S extends T> S update(S var1);

    <S extends T> Iterable<S> updateAll(Iterable<S> var1);

    <S extends T> S insert(S var1);

    <S extends T> Iterable<S> insertAll(Iterable<S> var1);

}
