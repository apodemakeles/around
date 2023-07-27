package io.apodemas.around.engine;

import java.util.concurrent.Executor;

/**
 * @author: Cao Zheng
 * @date: 2023/6/12
 * @description:
 */

public class EngineSettings {
    private Executor executor;
    private int partitionSize;

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
