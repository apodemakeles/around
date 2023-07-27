package io.apodemas.around.engine;

import io.apodemas.around.engine.task.ResourceType;

import java.util.Objects;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class Resource<T> implements ResourceType {
    private Class<T> clazz;
    private String alias;

    public Resource(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Resource(Class<T> clazz, String alias) {
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
        return clazz.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource<?> resource = (Resource<?>) o;
        return Objects.equals(clazz, resource.clazz) && Objects.equals(alias, resource.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, alias);
    }
}
