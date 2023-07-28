package io.apodemas.around.engine.com;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: Cao Zheng
 * @date: 2023/5/27
 * @description:
 */
public class ListKeyExtractNode<S, K> implements Function<List<S>, List<K>> {
    private List<Function<S, K>> extractors = new ArrayList<>();

    public ListKeyExtractNode(List<Function<S, K>> extractors) {
        this.extractors = extractors;
    }

    public List<K> apply(List<S> dataList) {
        Set<K> keys = new HashSet<>();
        for (S data : dataList) {
            for (Function<S, K> extractor : extractors) {
                final K key = extractor.apply(data);
                if (key != null) {
                    keys.add(key);
                }
            }
        }
        return keys.stream().collect(Collectors.toList());
    }
}
