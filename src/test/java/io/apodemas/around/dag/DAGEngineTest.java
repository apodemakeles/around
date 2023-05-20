package io.apodemas.around.dag;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author: Cao Zheng
 * @date: 2023/5/20
 * @description:
 */
public class DAGEngineTest {


    /**
     * 1 -> 2 -> 3
     */
    @Test
    public void traverse_case_1() {
        final Graph<Integer> graph = new Graph<>();
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        final DAGEngine<Integer> dag = graph.toDAG();
        final MockVisitor visitor = new MockVisitor();
        dag.traverse(visitor);

        assertCountAndSequence(graph, visitor);
    }

    /**
     * 1 --> 2 --> 5 --> 7
     * |     |     |     |
     * v     v     v     v
     * 3 --> 6 --> 8 <-- 4
     */
    @Test
    public void traverse_case_2() {
        final Graph<Integer> graph = new Graph<>();
        graph.addEdge(1, 2);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 5);
        graph.addEdge(3, 6);
        graph.addEdge(4, 6);
        graph.addEdge(5, 7);
        graph.addEdge(6, 8);
        graph.addEdge(7, 8);
        graph.addEdge(4, 8);
        final DAGEngine<Integer> dag = graph.toDAG();
        final MockVisitor visitor = new MockVisitor();
        dag.traverse(visitor);

        assertCountAndSequence(graph, visitor);
    }

    private void assertCountAndSequence(Graph<Integer> graph, MockVisitor visitor) {
        Assert.assertEquals(graph.vertices().size(), visitor.count());
        final HashMap<Integer, Integer> pos = visitor.getPos();
        for (Integer vertex : graph.vertices()) {
            final Integer vp = pos.get(vertex);
            for (Integer dest : graph.destinations(vertex)) {
                final Integer vd = pos.get(dest);
                Assert.assertTrue(
                        String.format("source :%d is at %d and destination %d is at %d",
                                vertex, vp, dest, vd),
                        vp < vd
                );
            }
        }
    }

    public static class MockVisitor implements DAGVisitor<Integer> {

        private int seq = 0;
        private HashMap<Integer, Integer> pos = new HashMap<>();

        @Override
        public void visit(Integer vertex) {
            pos.put(vertex, ++seq);
        }

        public int count() {
            return pos.size();
        }

        public HashMap<Integer, Integer> getPos() {
            return pos;
        }

    }
}
