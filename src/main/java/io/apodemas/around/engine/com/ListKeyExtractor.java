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
public class ListKeyExtractor<D, K> implements Function<List<D>, List<K>> {
    private List<Function<D, K>> extractors = new ArrayList<>();

    public void add(Function<D, K> extractor) {
        extractors.add(extractor);
    }

    public List<K> apply(List<D> dataList) {
        Set<K> keys = new HashSet<>();
        for (D data : dataList) {
            for (Function<D, K> extractor : extractors) {
                final K key = extractor.apply(data);
                if (key != null) {
                    keys.add(key);
                }
            }
        }
        return keys.stream().collect(Collectors.toList());
    }
}
