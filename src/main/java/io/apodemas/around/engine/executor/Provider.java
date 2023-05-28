package io.apodemas.around.engine.executor;

import io.apodemas.around.engine.task.ExecutionContext;
import io.apodemas.around.engine.task.ResourceType;
import io.apodemas.around.engine.task.TaskExecutor;

import java.util.function.Supplier;

/**
 * @author: Cao Zheng
 * @date: 2023/5/27
 * @description:
 */
public class Provider<R extends ResourceType, D> implements TaskExecutor<R> {
    private R dataType;
    private Supplier<D> supplier;

    public Provider(R dataType, Supplier<D> supplier) {
        this.dataType = dataType;
        this.supplier = supplier;
    }

    @Override
    public void execute(ExecutionContext<R> ctx) {
        ctx.set(dataType, supplier.get());
    }
}
