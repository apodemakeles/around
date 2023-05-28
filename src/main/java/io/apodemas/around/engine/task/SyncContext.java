package io.apodemas.around.engine.task;

/**
 * @author: Cao Zheng
 * @date: 2023/5/26
 * @description:
 */
public class SyncContext<R extends ResourceType> implements ExecutionContext<R> {
    private CommonContext<R> inner = new CommonContext<>();

    @Override
    public <T> void set(R type, T resource) {
        inner.set(type, resource);
    }

    @Override
    public <T> T get(R type) {
        return inner.get(type);
    }

}
