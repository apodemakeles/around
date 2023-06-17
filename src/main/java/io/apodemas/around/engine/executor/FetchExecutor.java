package io.apodemas.around.engine.executor;

import io.apodemas.around.engine.com.ForkFetcher;
import io.apodemas.around.engine.task.ExecutionContext;
import io.apodemas.around.engine.task.ResourceType;
import io.apodemas.around.engine.task.TaskAsyncExecutor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author: Cao Zheng
 * @date: 2023/6/13
 * @description:
 */
public class FetchExecutor<R extends ResourceType, S, D, K> implements TaskAsyncExecutor<R> {
    private R sourceRes;
    private R dstRes;
    private ForkFetcher<S, D, K> fetcher;
    private Executor executor;

    public FetchExecutor(R sourceRes, R dstRes, ForkFetcher<S, D, K> fetcher, Executor executor) {
        this.sourceRes = sourceRes;
        this.dstRes = dstRes;
        this.fetcher = fetcher;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<Void> execute(ExecutionContext<R> ctx) {
        final List<S> sources = ctx.get(sourceRes);
        return fetcher.fetchAsync(sources, executor).
                thenAccept(destinations -> ctx.set(dstRes, destinations));
    }
}
