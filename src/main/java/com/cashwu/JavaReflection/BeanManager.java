package com.cashwu.JavaReflection;

import com.cashwu.JavaReflection.annotation.Inject;
import com.cashwu.JavaReflection.annotation.Provides;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author cash.wu
 * @since 2024/06/04
 */
public class BeanManager {
    private static BeanManager instance = new BeanManager();
    private final Map<Class<?>, Supplier<?>> registry = new HashMap<>();

    public static BeanManager getInstance() {
        return instance;
    }

    private BeanManager() {
        List<Class<?>> classes = List.of(H2ConnectionProvider.class);

        for (Class<?> clss : classes) {

            Method[] methods = clss.getDeclaredMethods();

            for (Method method : methods) {

                Provides provides = method.getAnnotation(Provides.class);
                if (provides != null) {
                    Class<?> returnType = method.getReturnType();

                    Supplier<?> supplier = () -> {
                        try {
                            if (!Modifier.isStatic(method.getModifiers())) {
                                Object o = clss.getConstructor().newInstance();
                                return method.invoke(o);
                            }
                            else {
                                return method.invoke(null);
                            }

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    };

                    registry.put(returnType, supplier);
                }
            }
        }

    }

    public <T> T getInstance(Class<T> clss) {

        try {
            T t = clss.getConstructor().newInstance();

            Field[] fields = clss.getDeclaredFields();

            for (Field field : fields) {

                Inject inject = field.getAnnotation(Inject.class);

                if (inject != null) {
                    Class<?> injectFieldType = field.getType();
                    Supplier<?> supplier = registry.get(injectFieldType);

                    Object objectToInject = supplier.get();
                    field.setAccessible(true);
                    field.set(t, objectToInject);
                }
            }

            return t;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
