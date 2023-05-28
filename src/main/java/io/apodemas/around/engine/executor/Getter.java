package io.apodemas.around.engine.executor;

import io.apodemas.around.engine.task.ExecutionContext;
import io.apodemas.around.engine.task.ResourceType;
import io.apodemas.around.engine.task.TaskExecutor;

/**
 * @author: Cao Zheng
 * @date: 2023/5/27
 * @description:
 */
public class Getter<R extends ResourceType, D> implements TaskExecutor<R> {
    private R dataType;
    private D data;

    public Getter(R dataType) {
        this.dataType = dataType;
    }

    @Override
    public void execute(ExecutionContext<R> ctx) {
        data = ctx.get(dataType);
    }

    public D getData() {
        return data;
    }
}
