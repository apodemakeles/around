package io.apodemas.around.engine.task;

import java.util.concurrent.CompletableFuture;

/**
 * @author: Cao Zheng
 * @date: 2023/5/26
 * @description:
 */
public interface TaskAsyncExecutor<R extends ResourceType> {
    CompletableFuture<Void> execute(ExecutionContext<R> ctx);
}
