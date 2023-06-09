package io.apodemas.around.engine.executor;

import io.apodemas.around.engine.task.ExecutionContext;
import io.apodemas.around.engine.task.ResourceType;
import io.apodemas.around.engine.task.TaskAsyncExecutor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * @author: Cao Zheng
 * @date: 2023/6/17
 * @description:
 */
public class Provider<R extends ResourceType, S> implements TaskAsyncExecutor<R> {
    private R sourceRes;
    private Supplier<List<S>> supplier;
    private Executor executor;

    public Provider(R sourceRes, Supplier<List<S>> supplier, Executor executor) {
        this.sourceRes = sourceRes;
        this.supplier = supplier;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<Void> execute(ExecutionContext<R> ctx) {
        return CompletableFuture.supplyAsync(supplier, executor).thenAccept(sources -> ctx.set(sourceRes, sources));
    }
}
