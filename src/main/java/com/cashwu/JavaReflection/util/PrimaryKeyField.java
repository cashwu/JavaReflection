package com.cashwu.JavaReflection.util;

import com.cashwu.JavaReflection.annotation.PrimaryKey;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @author cash.wu
 * @since 2024/06/04
 */
public class PrimaryKeyField {
    private final Field field;
    private final PrimaryKey primaryKey;

    public PrimaryKeyField(Field field) {
        this.field = field;
        primaryKey = field.getAnnotation(PrimaryKey.class);
    }

    public String getName() {
        return primaryKey.name();
    }

    public Class<?> getType() {
        return field.getType();
    }

    public Field getField() {
        return field;
    }
}
