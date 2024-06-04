package com.cashwu.JavaReflection;

import com.cashwu.JavaReflection.util.ColumnField;
import com.cashwu.JavaReflection.util.Metamodel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author cash.wu
 * @since 2024/06/04
 */
public abstract class AbstractEntityManager<T> implements EntityManager<T> {

    private AtomicLong idGenerator = new AtomicLong(0L);

    @Override
    public void persist(T t) throws SQLException, IllegalAccessException {
        Metamodel metamodel = Metamodel.of(t.getClass());
        String sql = metamodel.buildInsertRequest();
        try (PreparedStatement statement = prepareStatementWith(sql).andParameters(t)) {
            statement.executeUpdate();
        }
    }

    @Override
    public T find(Class<T> clss,
                  Object primaryKey) throws
                                     SQLException,
                                     IllegalAccessException,
                                     InvocationTargetException,
                                     NoSuchMethodException,
                                     InstantiationException {

        Metamodel metamodel = Metamodel.of(clss);

        String sql = metamodel.buildSelectRequest();
        try (PreparedStatement statement = prepareStatementWith(sql).addPrimaryKey(primaryKey);
             ResultSet resultSet = statement.executeQuery()) {

            return buildInstanceFrom(clss, resultSet);
        }
    }

    private T buildInstanceFrom(Class<T> clss,
                                ResultSet resultSet) throws
                                                     NoSuchMethodException,
                                                     InvocationTargetException,
                                                     InstantiationException,
                                                     IllegalAccessException,
                                                     SQLException {

        Metamodel metamodel = Metamodel.of(clss);
        T t = clss.getConstructor().newInstance();

        Field primaryKeyField = metamodel.getPrimaryKey().getField();
        String primaryKeyColumnName = metamodel.getPrimaryKey().getName();
        Class<?> primaryKeyType = primaryKeyField.getType();

        resultSet.next();

        if (primaryKeyType == long.class) {
            long primaryKey = resultSet.getInt(primaryKeyColumnName);
            primaryKeyField.setAccessible(true);
            primaryKeyField.set(t, primaryKey);
        }

        for (ColumnField column : metamodel.getColumns()) {

            Field field = column.getField();

            field.setAccessible(true);
            Class<?> columnType = column.getType();
            String columnName = columnType.getName();

            if (columnType == int.class) {
                int value = resultSet.getInt(columnName);
                field.set(t, value);
            }
            else if (columnType == String.class) {
                String value = resultSet.getString(columnName);
                field.set(t, value);
            }

        }

        return t;
    }

    private PrepareStatementWrapper prepareStatementWith(String sql) throws SQLException {
        Connection connection = buildConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        return new PrepareStatementWrapper(preparedStatement);
    }

    public abstract Connection buildConnection() throws SQLException;


    private class PrepareStatementWrapper {
        private final PreparedStatement statement;

        public PrepareStatementWrapper(PreparedStatement preparedStatement) {
            this.statement = preparedStatement;
        }

        public PreparedStatement andParameters(T t) throws SQLException, IllegalAccessException {
            Metamodel metamodel = Metamodel.of(t.getClass());

            Class<?> primaryKeyType = metamodel.getPrimaryKey().getType();

            if (primaryKeyType == long.class) {
                //                preparedStatement.setLong(1, id);
                long id = idGenerator.incrementAndGet();
                statement.setLong(1, id);
                Field field = metamodel.getPrimaryKey().getField();
                field.setAccessible(true);
                field.set(t, id);
            }

            for (int i = 0; i < metamodel.getColumns().size(); i++) {
                ColumnField columnField = metamodel.getColumns().get(i);
                Class<?> fieldType = columnField.getType();
                Field field = columnField.getField();
                field.setAccessible(true);

                Object value = field.get(t);

                if (fieldType == int.class) {
                    statement.setInt(i + 2, (int) value);
                }
                else if (fieldType == String.class) {
                    statement.setString(i + 2, (String) value);
                }
            }

            return statement;
        }

        public PreparedStatement addPrimaryKey(Object primaryKey) throws SQLException {

            if (primaryKey.getClass() == long.class) {
                statement.setLong(1, (long) primaryKey);
            }

            return null;
        }
    }
}
