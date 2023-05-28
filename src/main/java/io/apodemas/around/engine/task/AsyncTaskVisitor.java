package io.apodemas.around.engine.task;

import io.apodemas.around.dag.DAGVisitor;

import java.util.List;

/**
 * @author: Cao Zheng
 * @date: 2023/5/28
 * @description:
 */
public class AsyncTaskVisitor<R extends ResourceType> implements DAGVisitor<TaskExecutor<R>> {

    private SyncContext<R> ctx = new SyncContext<>();

    @Override
    public void visit(List<TaskExecutor<R>> sources, TaskExecutor<R> current) {
        current.execute(ctx);
    }
}
