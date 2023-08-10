package io.apodemas.around.engine.node;

import io.apodemas.around.common.Assert;
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
public class ListAssembleNode<R extends Resource, S, D, K> implements ExecNode<R> {
    private R source;
    private R destination;
    private ListAssembler<S, D, K> assembler;

    public ListAssembleNode(R source, R destination, ListAssembler<S, D, K> assembler) {
        Assert.notNull(source, "source");
        Assert.notNull(destination, "destination");
        Assert.notNull(assembler, "assembler");

        this.source = source;
        this.destination = destination;
        this.assembler = assembler;
    }

    @Override
    public CompletableFuture<Void> execute(ExecContext<R> ctx) {
        final List<S> sources = ctx.get(source);
        final List<D> destinations = ctx.get(destination);
        assembler.accept(sources, destinations);
        return CompletableFuture.completedFuture(null);
    }
}
