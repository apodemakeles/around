package io.apodemas.around.engine;

import io.apodemas.around.dag.DAG;
import io.apodemas.around.engine.task.SyncContext;
import io.apodemas.around.engine.task.TaskVisitor;
import io.apodemas.around.engine.task.TaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class Engine<S> {
    private DAG<TaskExecutor<Resource<?>>> dag;
    private Resource<S> sourceResource;

    public Engine(DAG<TaskExecutor<Resource<?>>> dag, Resource<S> sourceResource) {
        this.dag = dag;
        this.sourceResource = sourceResource;
    }

    public void apply(List<S> sources, Executor executor) {
        final SyncContext<Resource<?>> ctx = new SyncContext<>();
        ctx.set(sourceResource, sources);
        dag.concurrentTraverse(new TaskVisitor<>(ctx, executor));
    }
}
