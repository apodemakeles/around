package io.apodemas.around.engine;

import io.apodemas.around.dag.DAG;
import io.apodemas.around.engine.task.SyncContext;
import io.apodemas.around.engine.task.TaskVisitor;
import io.apodemas.around.engine.task.TaskAsyncExecutor;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class Engine<S> {
    private DAG<TaskAsyncExecutor<Resource<?>>> dag;
    private Resource<S> sourceResource;
    private Executor executor;

    Engine(DAG<TaskAsyncExecutor<Resource<?>>> dag, Resource<S> sourceResource, Executor executor) {
        this.dag = dag;
        this.sourceResource = sourceResource;
        this.executor = executor;
    }

    public void apply(List<S> sources) {
        final SyncContext<Resource<?>> ctx = new SyncContext<>();
        ctx.set(sourceResource, sources);
        dag.concurrentTraverse(new TaskVisitor<>(ctx));
    }
}
