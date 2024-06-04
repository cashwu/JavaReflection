package com.cashwu.JavaReflection.util;

import com.cashwu.JavaReflection.annotation.Column;
import com.cashwu.JavaReflection.annotation.PrimaryKey;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author cash.wu
 * @since 2024/06/04
 */
public class Metamodel {

    private final Class<?> clss;

    public Metamodel(Class<?> clss) {

        this.clss = clss;
    }

    public static Metamodel of(Class<?> clss) {
        return new Metamodel(clss);
    }

    public PrimaryKeyField getPrimaryKey() {

        Field[] fields = clss.getDeclaredFields();

        for (Field field : fields) {
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            if (primaryKey != null) {
                PrimaryKeyField primaryKeyField = new PrimaryKeyField(field);

                return primaryKeyField;
            }
        }

        throw new IllegalArgumentException(
                "not primary key field in class " + clss.getSimpleName());
    }

    public List<ColumnField> getColumns() {

        List<ColumnField> columnFields = new ArrayList<>();
        Field[] fields = clss.getDeclaredFields();

        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                ColumnField columnField = new ColumnField(field);

                columnFields.add(columnField);
            }
        }

        return columnFields;
    }

    public String buildInsertRequest() {
        // insert into Person (id, name, age) value (?,?,?)

        String columnElement = buildColumnNames();

        String questionMarks = buildQuestionMarksElement();

        return String.format("insert into %s (%s) values (%s)", clss.getSimpleName(), columnElement,
                             questionMarks);
    }

    public String buildSelectRequest() {
        // select id, name age from Person where id = ?
        String columnElement = buildColumnNames();

        return String.format("select %s from %s where %s = ?", columnElement, clss.getSimpleName(),
                             getPrimaryKey().getName());
    }

    private String buildQuestionMarksElement() {
        int numberOfColumns = getColumns().size() + 1;
        return IntStream.range(0, numberOfColumns).mapToObj(i -> "?")
                        .collect(Collectors.joining(", "));
    }

    private String buildColumnNames() {
        String primaryKeyColumnName = getPrimaryKey().getName();

        List<String> columnNames = getColumns().stream().map(ColumnField::getName).toList();
        columnNames.add(0, primaryKeyColumnName);

        return String.join(", ", columnNames);
    }
}
