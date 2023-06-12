package io.apodemas.around.engine;

import io.apodemas.around.engine.task.ResourceType;

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
}
