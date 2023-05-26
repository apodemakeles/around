package io.apodemas.around.dag;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
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
        final Traverser<V> traverser = new Traverser<>();
        traverser.vertices = this.vertices;
        traverser.inDegree = copyInDegree();
        traverser.starts = this.starts;

        traverser.traverse(visitor);
    }

    public void concurrentTraverse(DAGVisitor<V> visitor, Executor executor) {
        final ConcurrentTraverser<V> traverser = new ConcurrentTraverser<>();
        traverser.vertices = this.vertices;
        traverser.inDegree = copyInDegree();
        traverser.starts = this.starts;

        traverser.traverse(visitor, executor);
    }

    private Map<V, Integer> copyInDegree() {
        final Map<V, Integer> copy = new HashMap<>();
        for (Map.Entry<V, Integer> entry : this.inDegree.entrySet()) {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }

    private static class Traverser<V> {
        private Map<V, Set<V>> vertices;
        private Map<V, Integer> inDegree;
        private Set<V> starts;
        private Map<V, List<V>> sourceMap = new HashMap<>();

        private void traverse(DAGVisitor<V> visitor) {
            for (V vertex : starts) {
                visit(vertex, visitor);
            }
        }

        private void visit(V current, DAGVisitor<V> visitor) {
            List<V> sources = this.sourceMap.getOrDefault(current, Collections.emptyList());
            visitor.visit(sources, current);
            for (V destination : vertices.get(current)) {
                final int d = inDegree.compute(destination, (vertex, degree) -> degree - 1);
                this.sourceMap.compute(destination, (k, v) -> {
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
        private Map<V, List<V>> sourceMap = new HashMap<>();

        private void traverse(DAGVisitor<V> visitor, Executor executor) {
            List<CompletableFuture<Void>> subFutures = new ArrayList<>();
            for (V vertex : starts) {
                subFutures.add(visit(vertex, visitor, executor));
            }
            try {
                CompletableFuture.allOf(subFutures.toArray(new CompletableFuture[0])).join();
            } catch (CompletionException e) {
                throw new DAGTraverseException("encountering an exception during traversal", e.getCause());
            } catch (Exception e) {
                throw new DAGTraverseException("encountering an exception during traversal", e);
            }
        }

        private CompletableFuture<Void> visit(V current, DAGVisitor<V> visitor, Executor executor) {
            final List<V> sources = getSources(current);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> visitor.visit(sources, current), executor);
            future = future.thenRun(() -> {
                List<CompletableFuture<Void>> subFutures = new ArrayList<>();
                for (V destination : vertices.get(current)) {
                    addSource(destination, current);
                    final int degree = decreaseAndGetDegree(destination);
                    if (degree == 0) {
                        final CompletableFuture<Void> subFuture = visit(destination, visitor, executor);
                        subFutures.add(subFuture);
                    }
                }
                if (subFutures.isEmpty()) {
                    return;
                }
                CompletableFuture.allOf(subFutures.toArray(new CompletableFuture[0])).join();
            });

            return future;
        }

        private synchronized List<V> getSources(V current) {
            return sourceMap.getOrDefault(current, Collections.emptyList());
        }

        private synchronized int decreaseAndGetDegree(V current) {
            return inDegree.compute(current, (vertex, degree) -> degree - 1);
        }

        private synchronized void addSource(V destination, V source) {
            List<V> sources = this.sourceMap.get(destination);
            if (sources == null) {
                sources = new ArrayList<>();
            }
            sources.add(source);
            this.sourceMap.put(destination, sources);
        }
    }
}
