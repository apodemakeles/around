package io.apodemas.around.engine.exec;

import java.util.concurrent.CompletableFuture;

/**
 * @author: Cao Zheng
 * @date: 2023/5/26
 * @description:
 */
public interface ExecNode<R extends Resource> {
    CompletableFuture<Void> execute(ExecContext<R> ctx);
}
