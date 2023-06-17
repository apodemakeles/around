package io.apodemas.around.dag;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author: Cao Zheng
 * @date: 2023/6/12
 * @description:
 */
public interface AsyncDAGVisitor<V> {
    CompletableFuture<Void> visit(List<V> sources, V current);
}
