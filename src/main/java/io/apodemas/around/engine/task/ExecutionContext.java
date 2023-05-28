package io.apodemas.around.engine.task;

/**
 * @author: Cao Zheng
 * @date: 2023/5/26
 * @description:
 */
public interface ExecutionContext<R extends ResourceType> {
    <T> void set(R type, T resource);

    <T> T get(R type);
}
