package com.es.base.common;


import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

public class RedisIncrementGenerator implements IdentifierGenerator {

    @Qualifier("generatorRedisTemplate")
    private RedisTemplate redisTemplate;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String key = String.format("prefix:%s", object.getClass().getName());
        Long id = redisTemplate.opsForValue().increment(key, 1);
        return id;
    }

}
