package io.apodemas.around.engine.task;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Cao Zheng
 * @date: 2023/5/26
 * @description:
 */
public class CommonContext<R extends ResourceType> implements ExecutionContext<R> {
    private Map<R, Object> map = new HashMap<>();

    @Override
    public <T> void set(R type, T resource) {
        if (map.containsKey(type)) {
            throw new ExecutionContextException("data of resource type " + type + " already been set");
        }
        map.put(type, resource);
    }

    @Override
    public <T> T get(R type) {
        if (!map.containsKey(type)) {
            throw new ExecutionContextException("data of resource type " + type + " not been set");
        }
        return (T) map.get(type);
    }
}
