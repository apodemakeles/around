package io.apodemas.around.engine;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author: Cao Zheng
 * @date: 2023/6/12
 * @description:
 */

public class EngineSettings {
    private Executor executor = DEFAULT_EXECUTOR;
    private int partitionSize;

    private static final Executor DEFAULT_EXECUTOR = Executors.newFixedThreadPool(10);

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public void setPartitionSize(int partitionSize) {
        this.partitionSize = partitionSize;
    }

    public Executor getExecutor() {
        return executor;
    }

    public int getPartitionSize() {
        return partitionSize;
    }
}
