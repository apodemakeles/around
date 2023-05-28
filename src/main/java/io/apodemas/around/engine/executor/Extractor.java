package io.apodemas.around.engine.executor;

import io.apodemas.around.engine.task.ExecutionContext;
import io.apodemas.around.engine.task.ResourceType;
import io.apodemas.around.engine.task.TaskExecutor;

import java.util.function.Function;

/**
 * @author: Cao Zheng
 * @date: 2023/5/27
 * @description:
 */
public class Extractor<R extends ResourceType, S, D> implements TaskExecutor<R> {
    private R srcType;
    private R dstType;
    private Function<S, D> fn;

    public Extractor(R srcType, R dstType, Function<S, D> fn) {
        this.srcType = srcType;
        this.dstType = dstType;
        this.fn = fn;
    }

    @Override
    public void execute(ExecutionContext<R> ctx) {
        final S source = ctx.get(srcType);
        ctx.set(dstType, fn.apply(source));
    }
}
