package com.cashwu.JavaReflection;

import com.cashwu.JavaReflection.annotation.Provides;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author cash.wu
 * @since 2024/06/04
 */
public class H2ConnectionProvider {

    @Provides
    public Connection buildConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("");
        return connection;
    }
}
