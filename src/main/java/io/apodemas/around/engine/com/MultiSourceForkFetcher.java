package io.apodemas.around.engine.com;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

/**
 * @author: Cao Zheng
 * @date: 2023/6/16
 * @description:
 */
public class MultiSourceForkFetcher<S, D, K> {
    private ListKeyExtractNode<S, K> keyExtractor;
    private Function<List<K>, List<D>> fetchFn;
    private int partitionSize;

    public MultiSourceForkFetcher(List<Function<S, K>> extractors, Function<List<K>, List<D>> fetchFn, int partitionSize) {
        this.keyExtractor = new ListKeyExtractNode<>(extractors);
        this.fetchFn = fetchFn;
        this.partitionSize = partitionSize;
    }

    public CompletableFuture<List<D>> fetchAsync(List<S> sources, Executor executor) {
        final List<K> keys = keyExtractor.apply(sources);
        final int total = keys.size();
        int shard = total / partitionSize + (total % partitionSize == 0 ? 0 : 1);
        CompletableFuture<List<D>>[] fetchFut = new CompletableFuture[shard];
        for (int i = 0; i < shard; i++) {
            final int fromIndex = i * partitionSize;
            final int toIndex = fromIndex + partitionSize > total ? total : fromIndex + partitionSize;
            final List<K> partKeys = keys.subList(fromIndex, toIndex);
            fetchFut[i] = fetch(partKeys, executor);
        }
        return CompletableFuture.allOf(fetchFut).thenApply((Void) -> {
            final List<D> destList = new ArrayList<>();
            for (CompletableFuture<List<D>> future : fetchFut) {
                destList.addAll(future.join());
            }
            return destList;
        });
    }

    private CompletableFuture<List<D>> fetch(List<K> keys, Executor executor) {
        return CompletableFuture.supplyAsync(() -> fetchFn.apply(keys), executor);
    }
}
