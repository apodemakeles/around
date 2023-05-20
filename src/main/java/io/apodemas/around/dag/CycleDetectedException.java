package io.apodemas.around.dag;

/**
 * @author: Cao Zheng
 * @date: 2023/5/19
 * @description:
 */
public class CycleDetectedException extends RuntimeException {
    public CycleDetectedException() {
        super("not a DAG");
    }
}
