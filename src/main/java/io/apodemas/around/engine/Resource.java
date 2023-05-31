package io.apodemas.around.engine;

import io.apodemas.around.engine.task.ResourceType;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class Resource<T> implements ResourceType {
    private Class<T> clazz;

    public Resource(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getClazz() {
        return clazz;
    }
}
