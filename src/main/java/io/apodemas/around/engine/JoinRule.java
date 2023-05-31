package io.apodemas.around.engine;

import io.apodemas.around.common.lambda.SFunction;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class JoinRule<L, R, K> {
    private Class<L> left;
    private Class<R> right;
    private SFunction<L, K> leftKeyGetter;
    private SFunction<R, K> rightKeyGetter;
    private SFunction<List<K>, List<R>> rightFetchFn;
    private BiConsumer<L, R> fillFn;

    public Class<L> getLeft() {
        return left;
    }

    public void setLeft(Class<L> left) {
        this.left = left;
    }

    public Class<R> getRight() {
        return right;
    }

    public void setRight(Class<R> right) {
        this.right = right;
    }

    public Function<L, K> getLeftKeyGetter() {
        return leftKeyGetter;
    }

    public void setLeftKeyGetter(SFunction<L, K> leftKeyGetter) {
        this.leftKeyGetter = leftKeyGetter;
    }

    public Function<R, K> getRightKeyGetter() {
        return rightKeyGetter;
    }

    public void setRightKeyGetter(SFunction<R, K> rightKeyGetter) {
        this.rightKeyGetter = rightKeyGetter;
    }

    public Function<List<K>, List<R>> getRightFetchFn() {
        return rightFetchFn;
    }

    public void setRightFetchFn(SFunction<List<K>, List<R>> rightFetchFn) {
        this.rightFetchFn = rightFetchFn;
    }

    public BiConsumer<L, R> getFillFn() {
        return fillFn;
    }

    public void setFillFn(BiConsumer<L, R> fillFn) {
        this.fillFn = fillFn;
    }
}
