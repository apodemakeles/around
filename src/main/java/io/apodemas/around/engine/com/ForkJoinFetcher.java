package io.apodemas.around.engine.com;

import io.apodemas.around.common.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author: Cao Zheng
 * @date: 2023/7/29
 * @description:
 */
public class ForkJoinFetcher<S, D, K> {
    private KeyExtractor<S, K> keyExtractor;
    private ListFetcher<K, D> fetcher;
    private int partitionSize;

    public ForkJoinFetcher(KeyExtractor<S, K> keyExtractor, ListFetcher<K, D> fetcher, int partitionSize) {
        Assert.notNull(keyExtractor, "keyExtractor");
        Assert.notNull(fetcher, "fetcher");
        if (partitionSize <= 0) {
            throw new IllegalArgumentException("partitionSize should greater than 0");
        }

        this.keyExtractor = keyExtractor;
        this.fetcher = fetcher;
        this.partitionSize = partitionSize;
    }

    public CompletableFuture<List<D>> fetchAsync(List<S> sources, Executor executor) {
        final List<K> keys = extractKeySet(sources).stream().collect(Collectors.toList());
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

    private Set<K> extractKeySet(List<S> sources) {
        final HashSet<K> keySet = new HashSet<>();
        for (S source : sources) {
            final K key = keyExtractor.apply(source);
            if (key != null) {
                keySet.add(key);
            }
        }

        return keySet;
    }

    private CompletableFuture<List<D>> fetch(List<K> keys, Executor executor) {
        return CompletableFuture.supplyAsync(() -> fetcher.apply(keys), executor);
    }
}
