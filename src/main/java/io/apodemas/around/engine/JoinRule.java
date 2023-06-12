package io.apodemas.around.engine;

import io.apodemas.around.common.lambda.SFunction;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class JoinRule<L, R, K> {
    private Resource<L> left;
    private Resource<R> right;
    private SFunction<L, K> leftKeyGetter;
    private SFunction<R, K> rightKeyGetter;
    private SFunction<List<K>, List<R>> rightFetchFn;
    private BiConsumer<L, R> fillFn;


}
