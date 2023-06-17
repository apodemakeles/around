package io.apodemas.around.engine.task;

import io.apodemas.around.dag.AsyncDAGVisitor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author: Cao Zheng
 * @date: 2023/5/28
 * @description:
 */
public class TaskVisitor<R extends ResourceType> implements AsyncDAGVisitor<TaskAsyncExecutor<R>> {
    private ExecutionContext<R> ctx;

    public TaskVisitor(ExecutionContext<R> ctx) {
        this.ctx = ctx;
    }

    @Override
    public CompletableFuture<Void> visit(List<TaskAsyncExecutor<R>> sources, TaskAsyncExecutor<R> current) {
        return current.execute(ctx);
    }
}
