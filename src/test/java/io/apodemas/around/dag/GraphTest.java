package io.apodemas.around.dag;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author: Cao Zheng
 * @date: 2023/5/20
 * @description:
 */
public class GraphTest {

    @Test(expected = CycleDetectedException.class)
    public void cyclic_case_1() {
        final Graph<Integer> graph = new Graph<>();
        graph.addEdge(1, 2);
        graph.addEdge(2, 1);
        graph.toDAG();
    }

    @Test(expected = CycleDetectedException.class)
    public void cyclic_case_2() {
        final Graph<Integer> graph = new Graph<>();
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(4, 5);
        graph.addEdge(5, 2);
        graph.toDAG();
    }

    @Test(expected = CycleDetectedException.class)
    public void cyclic_case_3() {
        final Graph<Integer> graph = new Graph<>();
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        graph.addEdge(5, 6);
        graph.addEdge(6, 1);
        graph.toDAG();
    }

    @Test
    public void graph_size_test() {
        final Graph<Integer> graph = new Graph<>();
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(4, 5);
        graph.addEdge(5, 2);
        Assert.assertEquals(5, graph.size());
    }

    @Test
    public void graph_size_should_be_zero() {
        final Graph<Integer> graph = new Graph<>();
        Assert.assertEquals(0, graph.size());
    }

    @Test
    public void in_degree_case_1() {
        final Graph<Integer> graph = new Graph<>();
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        final DAG dag = graph.toDAG();
        Assert.assertEquals(0, dag.getInDegree(1));
        Assert.assertEquals(1, dag.getInDegree(2));
        Assert.assertEquals(1, dag.getInDegree(3));
        Assert.assertEquals(1, dag.getInDegree(4));
        Assert.assertEquals(1, dag.getInDegree(5));
    }

    @Test
    public void in_degree_case_2() {
        final Graph<Integer> graph = new Graph<>();
        graph.addEdge(1, 5);
        graph.addEdge(2, 5);
        graph.addEdge(3, 5);
        graph.addEdge(4, 5);
        final DAG dag = graph.toDAG();
        Assert.assertEquals(0, dag.getInDegree(1));
        Assert.assertEquals(0, dag.getInDegree(2));
        Assert.assertEquals(0, dag.getInDegree(3));
        Assert.assertEquals(0, dag.getInDegree(4));
        Assert.assertEquals(4, dag.getInDegree(5));
    }
}
