package com.cashwu.JavaReflection.util;

import com.cashwu.JavaReflection.annotation.Column;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @author cash.wu
 * @since 2024/06/04
 */
public class ColumnField {
    private final Field field;
    private final Column column;

    public ColumnField(Field field) {
        this.field = field;
        this.column = field.getAnnotation(Column.class);
    }

    public String getName() {
        return column.name();
    }

    public Class<?> getType() {
        return field.getType();
    }

    public Field getField() {
        return field;
    }
}
