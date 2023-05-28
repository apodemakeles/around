package io.apodemas.around.engine.executor;

import io.apodemas.around.engine.task.ExecutionContext;
import io.apodemas.around.engine.task.ResourceType;
import io.apodemas.around.engine.task.TaskExecutor;

import java.util.function.BiConsumer;

/**
 * @author: Cao Zheng
 * @date: 2023/5/27
 * @description:
 */
public class Assembler<R extends ResourceType, S, D> implements TaskExecutor<R> {
    private R sourceType;
    private R destType;
    private BiConsumer<S, D> fn;

    public Assembler(R sourceType, R destType, BiConsumer<S, D> fn) {
        this.sourceType = sourceType;
        this.destType = destType;
        this.fn = fn;
    }

    @Override
    public void execute(ExecutionContext<R> ctx) {
        S source = ctx.get(sourceType);
        D dest = ctx.get(destType);
        fn.accept(source, dest);
    }
}
