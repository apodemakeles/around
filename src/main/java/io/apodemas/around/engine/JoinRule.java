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
    private TypedResource<L> left;
    private TypedResource<R> right;
    private SFunction<L, K> leftKeyGetter;
    private SFunction<R, K> rightKeyGetter;
    private SFunction<List<K>, List<R>> rightFetchFn;
    private BiConsumer<R, L> assembleFn;

    public void setLeft(TypedResource<L> left) {
        this.left = left;
    }

    public void setRight(TypedResource<R> right) {
        this.right = right;
    }

    public void setLeftKeyGetter(SFunction<L, K> leftKeyGetter) {
        this.leftKeyGetter = leftKeyGetter;
    }

    public void setRightKeyGetter(SFunction<R, K> rightKeyGetter) {
        this.rightKeyGetter = rightKeyGetter;
    }

    public void setRightFetchFn(SFunction<List<K>, List<R>> rightFetchFn) {
        this.rightFetchFn = rightFetchFn;
    }

    public void setAssembleFn(BiConsumer<R, L> assembleFn) {
        this.assembleFn = assembleFn;
    }

    public TypedResource<L> getLeft() {
        return left;
    }

    public TypedResource<R> getRight() {
        return right;
    }

    public SFunction<L, K> getLeftKeyGetter() {
        return leftKeyGetter;
    }

    public SFunction<R, K> getRightKeyGetter() {
        return rightKeyGetter;
    }

    public SFunction<List<K>, List<R>> getRightFetchFn() {
        return rightFetchFn;
    }

    public BiConsumer<R, L> getAssembleFn() {
        return assembleFn;
    }
}
