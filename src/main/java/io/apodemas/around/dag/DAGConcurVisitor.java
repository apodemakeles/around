package io.apodemas.around.dag;

import java.util.List;

/**
 * @author: Cao Zheng
 * @date: 2023/5/20
 * @description:
 */
public interface DAGConcurVisitor<V> {
    void visit(List<V> sources, V current);
}
