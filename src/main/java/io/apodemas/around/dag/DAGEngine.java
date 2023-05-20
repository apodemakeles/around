package io.apodemas.around.dag;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author: Cao Zheng
 * @date: 2023/5/19
 * @description:
 */
public class DAGEngine<V> {

    private Map<V, Set<V>> vertices;
    private Map<V, Integer> inDegree;
    private Set<V> starts;

    DAGEngine(Map<V, Set<V>> vertices, Map<V, Integer> inDegree) {
        this.vertices = vertices;
        this.inDegree = inDegree;
        starts = new HashSet<>();
        for (Map.Entry<V, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                starts.add(entry.getKey());
            }
        }
    }

    int getInDegree(V vertex) {
        if (!inDegree.containsKey(vertex)) {
            throw new IllegalArgumentException("the vertex is not in the graph");
        }
        return inDegree.get(vertex);
    }

    public void traverse(DAGVisitor<V> visitor) {
        for (V vertex : starts) {
            visit(vertex, visitor);
        }
    }

    private void visit(V current, DAGVisitor<V> visitor) {
        visitor.visit(current);
        for (V destination : vertices.get(current)) {
            final int d = inDegree.compute(destination, (vertex, degree) -> degree - 1);
            if (d == 0) {
                visit(destination, visitor);
            }
        }
    }
}
