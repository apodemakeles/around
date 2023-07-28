package io.apodemas.around.engine;

import io.apodemas.around.dag.Graph;
import io.apodemas.around.engine.com.MultiSourceForkFetcher;
import io.apodemas.around.engine.com.ListAssembleNode;
import io.apodemas.around.engine.node.AssembleExecutor;
import io.apodemas.around.engine.node.FetchExecutor;
import io.apodemas.around.engine.exec.ExecNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class RuleResolver {

    private EngineSettings settings;

    public RuleResolver(EngineSettings settings) {
        this.settings = settings;
    }

    public <S> Engine<S> resolve(Rules<S> rules) {
        // todo: 合并

        // todo: 检查class和alias的合法性

        Graph<ExecNode<TypedResource>> graph = new Graph<>();
        final ExecNode<TypedResource> firstAssembler = buildAssemblers(graph, rules);
        final TypedResource<S> startRes = rules.getSource();
        final Map<TypedResource, ExecNode<TypedResource>> fetcherMap = new HashMap<>();
        for (JoinRule<?, ?, ?> join : rules.getJoins()) {
            final MultiSourceForkFetcher fetcher = new MultiSourceForkFetcher(Collections.singletonList(join.getLeftKeyGetter()), join.getRightFetchFn(), settings.getPartitionSize());
            ExecNode<TypedResource> executor = new FetchExecutor(join.getLeft(), join.getRight(), fetcher, settings.getExecutor());
            if (join.getLeft().equals(startRes)) {
                fetcherMap.put(join.getRight(), executor);
            } else {
                if (!fetcherMap.containsKey(join.getLeft())) {
                    throw new RuleInvalidException(String.format("cannot find resource %s ", join.getLeft()));
                }
                final ExecNode<TypedResource> prvVertex = fetcherMap.get(join.getLeft());
                graph.addEdge(prvVertex, executor);
            }
            if (join.getAssembleFn() != null) {
                graph.addEdge(executor, firstAssembler);
            }
        }

        return new Engine<>(graph.toDAG(), startRes);
    }

    private <S> ExecNode<TypedResource> buildAssemblers(Graph<ExecNode<TypedResource>> graph, Rules<S> rules) {
        final TypedResource<S> dstRes = rules.getSource();
        ExecNode<TypedResource> first = null;
        ExecNode<TypedResource> prv = null;
        for (JoinRule<?, ?, ?> join : rules.getJoins()) {
            if (join.getAssembleFn() == null) {
                continue;
            }
            if (!join.getLeft().equals(dstRes)) {
                throw new RuleInvalidException(String.format("%s can not be destination of assembler", join.getLeft().name()));
            }
            final ListAssembleNode<?, ?, ?> listAssembler = new ListAssembleNode(join.getRightKeyGetter(), join.getLeftKeyGetter(), join.getAssembleFn());
            final AssembleExecutor<TypedResource, ?, ?, ?> cur = new AssembleExecutor(join.getRight(), join.getLeft(), listAssembler);
            if (first == null) {
                first = cur;
            } else {
                graph.addEdge(prv, cur);
            }
            prv = cur;
        }

        if (first == null) {
            throw new RuleInvalidException("need one at least assembler");
        }

        return first;
    }

}
