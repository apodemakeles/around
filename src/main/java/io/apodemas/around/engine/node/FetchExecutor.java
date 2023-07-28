package io.apodemas.around.engine.node;

import io.apodemas.around.engine.com.MultiSourceForkFetcher;
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
public class FetchExecutor<R extends Resource, S, D, K> implements ExecNode<R> {
    private R sourceRes;
    private R dstRes;
    private MultiSourceForkFetcher<S, D, K> fetcher;
    private Executor executor;

    public FetchExecutor(R sourceRes, R dstRes, MultiSourceForkFetcher<S, D, K> fetcher, Executor executor) {
        this.sourceRes = sourceRes;
        this.dstRes = dstRes;
        this.fetcher = fetcher;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<Void> execute(ExecContext<R> ctx) {
        final List<S> sources = ctx.get(sourceRes);
        return fetcher.fetchAsync(sources, executor).
                thenAccept(destinations -> ctx.set(dstRes, destinations));
    }
}
