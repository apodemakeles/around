package io.apodemas.around.engine;

import io.apodemas.around.dag.Graph;
import io.apodemas.around.engine.com.ForkJoinFetcher;
import io.apodemas.around.engine.com.ListAssembler;
import io.apodemas.around.engine.node.ListAssembleNode;
import io.apodemas.around.engine.node.ListFetchNode;
import io.apodemas.around.engine.exec.ExecNode;
import io.apodemas.around.engine.rule.*;

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
        rules.check();

        Graph<ExecNode<TypedResource>> graph = new Graph<>();

        final ExecNode<TypedResource> firstAssembler = buildAssemblers(graph, rules);

        final TypedResource<S> root = rules.root();

        buildJoins(graph, rules, firstAssembler);

        return new Engine<>(graph.toDAG(), root);
    }

    private <S> ExecNode<TypedResource> buildAssemblers(Graph<ExecNode<TypedResource>> graph, Rules<S> rules) {
        ExecNode<TypedResource> first = null;
        ExecNode<TypedResource> prv = null;

        for (Rule rule : rules.items()) {
            if (!(rule instanceof AssembleRule)) {
                continue;
            }
            final AssembleRule<?, ?, ?> assembleRule = (AssembleRule<?, ?, ?>) rule;
            final ListAssembleNode cur = buildAssemble(assembleRule);
            if (first == null) {
                first = cur;
            } else {
                graph.addEdge(prv, cur);
            }
            prv = cur;
        }

        return first;
    }

    private ListAssembleNode buildAssemble(AssembleRule rule) {
        final ListAssembler listAssembler = new ListAssembler(rule.sourceKeyExtractor(), rule.rootKeyExtractor(), rule.assembler());
        return new ListAssembleNode(rule.source(), rule.root(), listAssembler);
    }

    private <S> void buildJoins(Graph<ExecNode<TypedResource>> graph, Rules<S> rules, ExecNode<TypedResource> firstAssembler) {
        final TypedResource<S> root = rules.root();
        final Map<TypedResource, ExecNode<TypedResource>> fetcherMap = new HashMap<>();
        for (Rule rule : rules.items()) {
            if (!(rule instanceof JoinRule)) {
                continue;
            }
            final JoinRule<?, ?, ?> joinRule = (JoinRule<?, ?, ?>) rule;
            final ForkJoinFetcher<S, ?, ?> fetcher = new ForkJoinFetcher(joinRule.sourceKeyExtractor(), joinRule.fetcher(), settings.getPartitionSize());
            ExecNode<TypedResource> fetchNode = new ListFetchNode(joinRule.source(), joinRule.dest(), fetcher, settings.getExecutor());
            if (joinRule.source().equals(root)) {
                fetcherMap.put(joinRule.dest(), fetchNode);
                graph.addEdge(fetchNode, firstAssembler);
            } else {
                if (!fetcherMap.containsKey(joinRule.source())) {
                    // todo: 不再依赖于rules的顺序，用一个数据结构暂存找不到source的join
                    throw new RuleInvalidException(String.format("cannot find resource %s", joinRule.source()));
                }
                final ExecNode<TypedResource> prvVertex = fetcherMap.get(joinRule.source());
                graph.addEdge(prvVertex, fetchNode);
            }
        }
    }
}
