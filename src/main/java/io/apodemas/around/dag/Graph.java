package io.apodemas.around.dag;

import com.sun.javafx.collections.UnmodifiableListSet;

import javax.print.attribute.UnmodifiableSetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Cao Zheng
 * @date: 2023/5/19
 * @description:
 */
public class Graph<V> {

    private Map<V, Set<V>> vertices = new HashMap<>();

    public void addEdge(V vertex1, V vertex2) {
        final Set<V> out = vertices.computeIfAbsent(vertex1, v -> new HashSet<>());
        out.add(vertex2);
        vertices.computeIfAbsent(vertex2, v -> new HashSet<>());
    }

    public List<V> vertices() {
        return vertices.keySet().stream().collect(Collectors.toList());
    }

    public List<V> destinations(V vertex) {
        final Set<V> set = vertices.get(vertex);
        if (set == null) {
            throw new IllegalArgumentException("the vertex is not in the graph");
        }
        return new ArrayList<>(set);
    }

    public int size() {
        return vertices.size();
    }

    public DAGEngine<V> toDAG() {
        final HashSet<V> visited = new HashSet<>();
        final HashSet<V> rec = new HashSet<>();
        final Map<V, Integer> inDegree = new HashMap<>();
        for (V vertex : vertices.keySet()) {
            inDegree.put(vertex, 0);
        }

        for (V vertex : vertices.keySet()) {
            dfs(vertex, visited, rec, inDegree);
        }

        return new DAGEngine<>(vertices, inDegree);
    }

    private void dfs(V current, Set<V> visited, Set<V> rec, Map<V, Integer> inDegree) {
        if (rec.contains(current)) {
            throw new CycleDetectedException();
        }
        if (visited.contains(current)) {
            return;
        }
        visited.add(current);
        rec.add(current);

        for (V destination : vertices.get(current)) {
            inDegree.compute(destination, (vertex, degree) -> degree + 1);
            dfs(destination, visited, rec, inDegree);
        }
        rec.remove(current);
    }
}
