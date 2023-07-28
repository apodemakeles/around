package io.apodemas.around.engine.exec;

/**
 * @author: Cao Zheng
 * @date: 2023/5/26
 * @description:
 */
public interface ExecContext<R extends Resource> {
    <T> void set(R type, T resource);

    <T> T get(R type);
}
