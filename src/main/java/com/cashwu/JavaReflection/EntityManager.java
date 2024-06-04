package com.cashwu.JavaReflection;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * @author cash.wu
 * @since 2024/06/04
 */
public interface EntityManager<T> {

    static <T> EntityManager<T> of(Class<T> clss) {
        return new H2EntityManager<>();
    }

    void persist(T t) throws SQLException, IllegalAccessException;

    T find(Class<T> tClass,
           Object primaryKey) throws
                              SQLException,
                              IllegalAccessException,
                              InvocationTargetException,
                              NoSuchMethodException,
                              InstantiationException;
}
