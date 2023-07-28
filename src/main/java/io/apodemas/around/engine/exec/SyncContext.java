package io.apodemas.around.engine.exec;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Cao Zheng
 * @date: 2023/5/26
 * @description:
 */
public class SyncContext<R extends Resource> implements ExecContext<R> {
    private Map<R, Object> map = new HashMap<>();

    @Override
    public synchronized <T> void set(R type, T resource) {
        if (map.containsKey(type)) {
            throw new IllegalStateException("resource" + type + " already been set");
        }
        map.put(type, resource);
    }

    @Override
    public synchronized <T> T get(R type) {
        if (!map.containsKey(type)) {
            throw new IllegalStateException("cannot find resource " + type.name());
        }
        return (T) map.get(type);
    }

}
