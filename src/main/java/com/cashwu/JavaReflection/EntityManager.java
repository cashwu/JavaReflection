package com.cashwu.JavaReflection;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * @author cash.wu
 * @since 2024/06/04
 */
public interface EntityManager<T> {

    void persist(T t) throws SQLException, IllegalAccessException;

    T find(Class<T> tClass,
           Object primaryKey) throws
                              SQLException,
                              IllegalAccessException,
                              InvocationTargetException,
                              NoSuchMethodException,
                              InstantiationException;
}
