package io.apodemas.around.dag;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Cao Zheng
 * @date: 2023/5/24
 * @description:
 */
public class DAGConcurrentEngineTest {

    /**
     * 1 -> 2 -> 3
     */
    @Test
    public void traverse_case_1() {
        final MockAssembler assembler = new MockAssembler(Executors.newCachedThreadPool());
        assembler.add(1, 2);
        assembler.add(2, 3);
        final DAG<Integer> dag = assembler.toDAG();
        final MockVisitor visitor = assembler.toVisitor();
        dag.concurrentTraverse(visitor);
        assertCountAndSequence(assembler.getVertices(), visitor);
    }

    @Test
    public void traverse_case_2() {
        final MockAssembler assembler = new MockAssembler(Executors.newCachedThreadPool());
        assembler.add(1, 2);
        assembler.add(1, 3);
        assembler.add(2, 4);
        assembler.add(3, 4);
        final DAG<Integer> dag = assembler.toDAG();
        final MockVisitor visitor = assembler.toVisitor();
        dag.concurrentTraverse(visitor);
        assertCountAndSequence(assembler.getVertices(), visitor);
    }

    @Test
    public void traverse_case_3() {
        final MockAssembler assembler = new MockAssembler(Executors.newFixedThreadPool(1));
        assembler.add(1, 2);
        assembler.add(1, 3);
        assembler.add(2, 4);
        assembler.add(3, 4);
        final DAG<Integer> dag = assembler.toDAG();
        final MockVisitor visitor = assembler.toVisitor();
        dag.concurrentTraverse(visitor);
        assertCountAndSequence(assembler.getVertices(), visitor);
    }

    @Test
    public void traverse_case_4() {
        final MockAssembler assembler = new MockAssembler(Executors.newCachedThreadPool());
        assembler.add(1, 2);
        assembler.add(1, 3);
        assembler.add(1, 4);
        assembler.add(2, 5);
        assembler.add(2, 6);
        assembler.add(3, 6);
        assembler.add(5, 7);
        assembler.add(6, 8);
        assembler.add(7, 8);
        assembler.add(4, 8);
        final DAG<Integer> dag = assembler.toDAG();
        final MockVisitor visitor = assembler.toVisitor();
        dag.concurrentTraverse(visitor);
        assertCountAndSequence(assembler.getVertices(), visitor);
    }

    @Test
    public void traverse_case_5() {
        final MockAssembler assembler = new MockAssembler(Executors.newFixedThreadPool(1));
        assembler.add(1, 2);
        assembler.add(1, 3);
        assembler.add(1, 4);
        assembler.add(2, 5);
        assembler.add(2, 6);
        assembler.add(3, 6);
        assembler.add(5, 7);
        assembler.add(6, 8);
        assembler.add(7, 8);
        assembler.add(4, 8);
        final DAG<Integer> dag = assembler.toDAG();
        final MockVisitor visitor = assembler.toVisitor();
        dag.concurrentTraverse(visitor);
        assertCountAndSequence(assembler.getVertices(), visitor);
    }

    private void assertCountAndSequence(Map<Integer, Set<Integer>> vertices, MockVisitor visitor) {
        Assert.assertEquals(vertices.size(), visitor.count());
        final ConcurrentHashMap<Integer, Integer> pos = visitor.getPos();
        for (Map.Entry<Integer, Set<Integer>> entry : vertices.entrySet()) {
            final Integer source = entry.getKey();
            final Integer srcIndex = pos.get(source);
            final Set<Integer> destinations = entry.getValue();
            for (Integer destination : destinations) {
                final Integer destIndex = pos.get(destination);
                Assert.assertTrue(
                        String.format("source: %d is at %d and destination: %d is at %d",
                                source, srcIndex, destination, destIndex),
                        srcIndex < destIndex
                );
            }
        }
    }

    public static class MockAssembler {
        private Graph<Integer> graph = new Graph<>();
        private Map<Integer, List<Integer>> sources = new HashMap<>();
        private Executor executor;

        public MockAssembler(Executor executor) {
            this.executor = executor;
        }

        public void add(int source, int destination) {
            graph.addEdge(source, destination);

            List<Integer> sources = this.sources.get(destination);
            if (sources == null) {
                sources = new ArrayList<>();
                this.sources.put(destination, sources);
            }
            sources.add(source);
            this.sources.put(destination, sources);
        }

        public DAG<Integer> toDAG() {
            return graph.toDAG();
        }

        public MockVisitor toVisitor() {
            return new MockVisitor(sources, executor);
        }

        public Map<Integer, Set<Integer>> getVertices() {
            return graph.vertexMap();
        }
    }

    public static class MockVisitor implements AsyncDAGVisitor<Integer> {

        private AtomicInteger seq = new AtomicInteger(0);
        private ConcurrentHashMap<Integer, Integer> pos = new ConcurrentHashMap<>();
        private Map<Integer, List<Integer>> sources;
        private Executor executor;

        public MockVisitor(Map<Integer, List<Integer>> sources, Executor executor) {
            this.sources = sources;
            this.executor = executor;
        }

        @Override
        public CompletableFuture<Void> visit(List<Integer> sources, Integer current) {
            return CompletableFuture.runAsync(() -> {
                System.out.println("visit " + current);
                pos.put(current, seq.incrementAndGet());
                final Set<Integer> expectedSources = new HashSet<>(sources);
                final HashSet<Integer> actualSources = new HashSet<>(this.sources.getOrDefault(current, Collections.EMPTY_LIST));
                Assert.assertEquals("source test fail", expectedSources, actualSources);
            }, executor);
        }

        public int count() {
            return pos.size();
        }

        public ConcurrentHashMap<Integer, Integer> getPos() {
            return pos;
        }
    }
}
