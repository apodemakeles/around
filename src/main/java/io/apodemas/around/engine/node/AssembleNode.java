package io.apodemas.around.engine.node;

import io.apodemas.around.engine.com.ListAssembler;
import io.apodemas.around.engine.exec.ExecContext;
import io.apodemas.around.engine.exec.Resource;
import io.apodemas.around.engine.exec.ExecNode;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author: Cao Zheng
 * @date: 2023/6/16
 * @description:
 */
public class AssembleNode<R extends Resource, S, D, K> implements ExecNode<R> {
    private R sourceRes;
    private R dstRes;
    private ListAssembler<S, D, K> assembler;

    public AssembleNode(R sourceRes, R dstRes, ListAssembler<S, D, K> assembler) {
        this.sourceRes = sourceRes;
        this.dstRes = dstRes;
        this.assembler = assembler;
    }

    @Override
    public CompletableFuture<Void> execute(ExecContext<R> ctx) {
        final List<S> sources = ctx.get(sourceRes);
        final List<D> destinations = ctx.get(dstRes);
        assembler.accept(sources, destinations);
        return CompletableFuture.completedFuture(null);
    }
}
