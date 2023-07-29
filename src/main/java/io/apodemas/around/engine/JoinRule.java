package io.apodemas.around.engine;

import io.apodemas.around.common.lambda.SFunction;
import io.apodemas.around.engine.com.Assembler;
import io.apodemas.around.engine.com.KeyExtractor;
import io.apodemas.around.engine.com.ListFetcher;

import java.util.List;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class JoinRule<L, R, K> {
    private TypedResource<L> left;
    private TypedResource<R> right;
    private KeyExtractor<L, K> leftKeyExtractor;
    private KeyExtractor<R, K> rightKeyExtractor;
    private ListFetcher<K, R> fetcher;
    private Assembler<R, L> assembler;

    public void setLeft(TypedResource<L> left) {
        this.left = left;
    }

    public void setRight(TypedResource<R> right) {
        this.right = right;
    }

    public void setLeftKeyExtractor(KeyExtractor<L, K> leftKeyExtractor) {
        this.leftKeyExtractor = leftKeyExtractor;
    }

    public void setRightKeyExtractor(KeyExtractor<R, K> rightKeyExtractor) {
        this.rightKeyExtractor = rightKeyExtractor;
    }

    public void setFetcher(ListFetcher<K, R> fetcher) {
        this.fetcher = fetcher;
    }

    public void setAssembler(Assembler<R, L> assembler) {
        this.assembler = assembler;
    }

    public TypedResource<L> getLeft() {
        return left;
    }

    public TypedResource<R> getRight() {
        return right;
    }

    public KeyExtractor<L, K> getLeftKeyExtractor() {
        return leftKeyExtractor;
    }

    public KeyExtractor<R, K> getRightKeyExtractor() {
        return rightKeyExtractor;
    }

    public ListFetcher<K, R> getFetcher() {
        return fetcher;
    }

    public Assembler<R, L> getAssembler() {
        return assembler;
    }
}
