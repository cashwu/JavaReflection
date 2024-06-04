package com.cashwu.JavaReflection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author cash.wu
 * @since 2024/06/04
 */
public class H2EntityManager<T> extends AbstractEntityManager<T> {

    @Override
    public Connection buildConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("");
        return connection;
    }
}
