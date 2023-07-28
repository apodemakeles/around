package io.apodemas.around.engine;

import io.apodemas.around.engine.exec.Resource;

import java.util.Objects;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class TypedResource<T> implements Resource {
    private Class<T> clazz;
    private String alias;

    public TypedResource(Class<T> clazz) {
        this.clazz = clazz;
    }

    public TypedResource(Class<T> clazz, String alias) {
        this.clazz = clazz;
        this.alias = alias;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public String getAlias() {
        return alias;
    }

    public String name() {
        if (alias != null) {
            return alias;
        }
        return clazz.getSimpleName(); // simple name is enough ?
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypedResource<?> resource = (TypedResource<?>) o;
        return Objects.equals(clazz, resource.clazz) && Objects.equals(alias, resource.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, alias);
    }
}
