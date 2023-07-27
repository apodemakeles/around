package io.apodemas.around.engine;

import io.apodemas.around.dag.Graph;
import io.apodemas.around.engine.com.ForkFetcher;
import io.apodemas.around.engine.com.ListAssembler;
import io.apodemas.around.engine.executor.AssembleExecutor;
import io.apodemas.around.engine.executor.FetchExecutor;
import io.apodemas.around.engine.task.TaskAsyncExecutor;

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
 
        // todo: 检查alias

        Graph<TaskAsyncExecutor<Resource>> graph = new Graph<>();
        final TaskAsyncExecutor<Resource> firstAssembler = buildAssemblers(graph, rules);
        final Resource<S> startRes = rules.getSource();
        final Map<Resource, TaskAsyncExecutor<Resource>> fetcherMap = new HashMap<>();
        for (JoinRule<?, ?, ?> join : rules.getJoins()) {
            final ForkFetcher fetcher = new ForkFetcher(Collections.singletonList(join.getLeftKeyGetter()), join.getRightFetchFn(), settings.getPartitionSize());
            TaskAsyncExecutor<Resource> executor = new FetchExecutor(join.getLeft(), join.getRight(), fetcher, settings.getExecutor());
            if (join.getLeft().equals(startRes)) {
                fetcherMap.put(join.getRight(), executor);
            } else {
                if (!fetcherMap.containsKey(join.getLeft())) {
                    throw new RuleInvalidException(String.format("cannot find resource %s ", join.getLeft()));
                }
                final TaskAsyncExecutor<Resource> prvVertex = fetcherMap.get(join.getLeft());
                graph.addEdge(prvVertex, executor);
            }
            if (join.getAssembleFn() != null) {
                graph.addEdge(executor, firstAssembler);
            }
        }

        return new Engine<S>(graph.toDAG(), startRes);
    }

    private <S> TaskAsyncExecutor<Resource> buildAssemblers(Graph<TaskAsyncExecutor<Resource>> graph, Rules<S> rules) {
        final Resource<S> dstRes = rules.getSource();
        TaskAsyncExecutor<Resource> first = null;
        TaskAsyncExecutor<Resource> prv = null;
        for (JoinRule<?, ?, ?> join : rules.getJoins()) {
            if (join.getAssembleFn() == null) {
                continue;
            }
            if (!join.getLeft().equals(dstRes)) {
                throw new RuleInvalidException(String.format("%s can not be destination of assembler", join.getLeft().name()));
            }
            final ListAssembler<?, ?, ?> listAssembler = new ListAssembler(join.getRightKeyGetter(), join.getLeftKeyGetter(), join.getAssembleFn());
            final AssembleExecutor<Resource, ?, ?, ?> cur = new AssembleExecutor(join.getRight(), join.getLeft(), listAssembler);
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
