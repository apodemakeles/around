package io.apodemas.around.engine.task;

/**
 * @author: Cao Zheng
 * @date: 2023/5/26
 * @description:
 */
public interface TaskExecutor<R extends ResourceType> {
    void execute(ExecutionContext<R> ctx);
}
