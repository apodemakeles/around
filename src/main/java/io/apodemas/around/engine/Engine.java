package io.apodemas.around.engine;

import io.apodemas.around.dag.DAG;
import io.apodemas.around.engine.exec.SyncContext;
import io.apodemas.around.engine.exec.NodeVisitor;
import io.apodemas.around.engine.exec.ExecNode;

import java.util.List;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class Engine<S> {
    private DAG<ExecNode<TypedResource>> dag;
    private TypedResource<S> sourceResource;

    Engine(DAG<ExecNode<TypedResource>> dag, TypedResource<S> sourceResource) {
        this.dag = dag;
        this.sourceResource = sourceResource;
    }

    public void apply(List<S> sources) {
        final SyncContext<TypedResource<?>> ctx = new SyncContext<>();
        ctx.set(sourceResource, sources);
        dag.concurrentTraverse(new NodeVisitor(ctx));
    }
}
