package io.apodemas.around.dag;

import com.sun.xml.internal.ws.util.CompletedFuture;

import java.util.*;
import java.util.concurrent.Executor;

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
        final Traverser<V> traverser = getTraverser();
        traverser.traverse(visitor);
    }

    private Traverser<V> getTraverser() {
        final Traverser<V> traverser = new Traverser<>();
        traverser.vertices = this.vertices;
        traverser.inDegree = copyInDegree();
        traverser.starts = this.starts;

        return traverser;
    }

    private Map<V, Integer> copyInDegree() {
        final Map<V, Integer> inDegree = new HashMap<>();
        for (Map.Entry<V, Integer> entry : this.inDegree.entrySet()) {
            inDegree.put(entry.getKey(), entry.getValue());
        }
        return inDegree;
    }

    private static class Traverser<V> {
        private Map<V, Set<V>> vertices;
        private Map<V, Integer> inDegree;
        private Set<V> starts;
        private Map<V, List<V>> sources = new HashMap<>();

        private void traverse(DAGVisitor<V> visitor) {
            for (V vertex : starts) {
                visit(vertex, visitor);
            }
        }

        private void visit(V current, DAGVisitor<V> visitor) {
            List<V> sources = this.sources.getOrDefault(current, Collections.EMPTY_LIST);
            visitor.visit(sources, current);
            for (V destination : vertices.get(current)) {
                final int d = inDegree.compute(destination, (vertex, degree) -> degree - 1);
                this.sources.compute(destination, (k, v) -> {
                    if (v == null) {
                        v = new ArrayList<>();
                    }
                    v.add(current);
                    return v;
                });
                if (d == 0) {
                    visit(destination, visitor);
                }
            }
        }
    }

    private static class ConcurrentTraverser<V> {
        private Map<V, Set<V>> vertices;
        private Map<V, Integer> inDegree;
        private Set<V> starts;
        private Map<V, List<V>> sources = new HashMap<>();

        private void traverse(DAGConcurVisitor<V> visitor, Executor executor) {

        }

        private CompletedFuture<Void> visit(V current, DAGVisitor<V> visitor) {
            return null;
        }
    }
}
