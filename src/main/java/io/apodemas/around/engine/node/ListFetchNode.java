package io.apodemas.around.engine.node;

import io.apodemas.around.common.Assert;
import io.apodemas.around.engine.com.ForkJoinFetcher;
import io.apodemas.around.engine.exec.ExecContext;
import io.apodemas.around.engine.exec.Resource;
import io.apodemas.around.engine.exec.ExecNode;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author: Cao Zheng
 * @date: 2023/6/13
 * @description:
 */
public class ListFetchNode<R extends Resource, S, D, K> implements ExecNode<R> {
    private R source;
    private R destination;
    private ForkJoinFetcher<S, D, K> fetcher;
    private Executor executor;

    public ListFetchNode(R source, R destination, ForkJoinFetcher<S, D, K> fetcher, Executor executor) {
        Assert.notNull(source, "source");
        Assert.notNull(destination, "destination");
        Assert.notNull(fetcher, "fetcher");
        Assert.notNull(executor, "executor");

        this.source = source;
        this.destination = destination;
        this.fetcher = fetcher;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<Void> execute(ExecContext<R> ctx) {
        final List<S> sources = ctx.get(source);
        return fetcher.fetchAsync(sources, executor).
                thenAccept(destinations -> ctx.set(destination, destinations));
    }
}
