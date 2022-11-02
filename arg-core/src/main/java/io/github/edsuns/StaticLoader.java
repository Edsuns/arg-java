package io.github.edsuns;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author edsuns@qq.com
 * @date 2022/10/30
 */
class StaticLoader<T> {

    private static final ConcurrentMap<Class<?>, StaticLoader<?>> CACHE = new ConcurrentHashMap<>();

    private final Class<? extends T> clz;
    private final List<Field> fields;
    private Field serialVersionUIDField;
    private int idx = 0;

    StaticLoader(Class<? extends T> clz, List<Field> fields) {
        this.clz = clz;
        this.fields = fields;
    }

    Field currentStaticField() {
        if (idx >= fields.size()) {
            throw new IllegalStateException("cause by illegal usage");
        }
        Field field = fields.get(idx);
        idx++;
        if (idx == fields.size()) {
            close();
        }
        return field;
    }

    private Field getSerialVersionUIDField() {
        if (serialVersionUIDField != null) {
            return serialVersionUIDField;
        }
        Field[] declaredFields = clz.getDeclaredFields();
        for (Field field : declaredFields) {
            if ("serialVersionUID".equals(field.getName())) {
                field.setAccessible(true);
                serialVersionUIDField = field;
                return field;
            }
        }
        throw new IllegalStateException("serialVersionUID not declared in " + clz);
    }

    long getSerialVersionUID() {
        try {
            return getSerialVersionUIDField().getLong(clz);
        } catch (IllegalAccessException e) {
            throw new InternalError(e);
        }
    }

    private void close() {
        CACHE.remove(clz);
    }

    static <T> StaticLoader<T> get(T obj) {
        if (obj instanceof Class) {
            throw new IllegalArgumentException("obj instanceof Class");
        }
        @SuppressWarnings("unchecked")
        Class<? extends T> clz = (Class<? extends T>) obj.getClass();
        return get(clz);
    }

    @SuppressWarnings("unchecked")
    static <T> StaticLoader<T> get(Class<?> clz) {
        if (clz == Class.class) {
            throw new IllegalArgumentException("clz == Class.class");
        }
        return (StaticLoader<T>) CACHE.computeIfAbsent(clz, k -> new StaticLoader<>(clz, getCompatibleStaticFields(clz)));
    }

    private static List<Field> getCompatibleStaticFields(Class<?> clz) {
        List<Field> result = new ArrayList<>();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && (
                field.getType().isAssignableFrom(clz) || field.getType() == Obj.class
            )) {
                result.add(field);
            }
        }
        return result;
    }
}
