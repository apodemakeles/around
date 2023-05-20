package io.apodemas.around.dag;

/**
 * @author: Cao Zheng
 * @date: 2023/5/20
 * @description:
 */
public interface DAGVisitor<V> {
    void visit(V vertex);
}
