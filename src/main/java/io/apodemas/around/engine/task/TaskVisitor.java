package io.apodemas.around.engine.task;

import io.apodemas.around.dag.AsyncDAGVisitor;
import io.apodemas.around.dag.DAGVisitor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author: Cao Zheng
 * @date: 2023/5/28
 * @description:
 */
public class TaskVisitor<R extends ResourceType> implements AsyncDAGVisitor<TaskExecutor<R>> {

    private ExecutionContext<R> ctx;

    private Executor executor;

    public TaskVisitor(ExecutionContext<R> ctx, Executor executor) {
        this.ctx = ctx;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<Void> visit(List<TaskExecutor<R>> sources, TaskExecutor<R> current) {
        return CompletableFuture.runAsync(() -> current.execute(ctx), executor);
    }
}
