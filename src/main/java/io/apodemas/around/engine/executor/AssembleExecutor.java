package io.apodemas.around.engine.executor;

import io.apodemas.around.engine.com.ListAssembler;
import io.apodemas.around.engine.task.ExecutionContext;
import io.apodemas.around.engine.task.ResourceType;
import io.apodemas.around.engine.task.TaskAsyncExecutor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author: Cao Zheng
 * @date: 2023/6/16
 * @description:
 */
public class AssembleExecutor<R extends ResourceType, S, D, K> implements TaskAsyncExecutor<R> {
    private R sourceRes;
    private R dstRes;
    private ListAssembler<S, D, K> assembler;

    public AssembleExecutor(R sourceRes, R dstRes, ListAssembler<S, D, K> assembler) {
        this.sourceRes = sourceRes;
        this.dstRes = dstRes;
        this.assembler = assembler;
    }

    @Override
    public CompletableFuture<Void> execute(ExecutionContext<R> ctx) {
        final List<S> sources = ctx.get(sourceRes);
        final List<D> destinations = ctx.get(dstRes);
        assembler.accept(sources, destinations);
        return CompletableFuture.completedFuture(null);
    }
}
