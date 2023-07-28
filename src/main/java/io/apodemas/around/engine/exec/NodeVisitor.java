package io.apodemas.around.engine.exec;

import io.apodemas.around.dag.AsyncDAGVisitor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author: Cao Zheng
 * @date: 2023/5/28
 * @description:
 */
public class NodeVisitor<R extends Resource> implements AsyncDAGVisitor<ExecNode<R>> {
    private ExecContext<R> ctx;

    public NodeVisitor(ExecContext<R> ctx) {
        this.ctx = ctx;
    }

    @Override
    public CompletableFuture<Void> visit(List<ExecNode<R>> parents, ExecNode<R> current) {
        return current.execute(ctx);
    }
}
