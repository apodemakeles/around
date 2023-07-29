package io.apodemas.around.engine.com;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: Cao Zheng
 * @date: 2023/5/27
 * @description:
 */
public class ListAssembler<S, D, K> implements BiConsumer<List<S>, List<D>> {
    private KeyExtractor<S, K> srcKeyExtractor;
    private KeyExtractor<D, K> dstKeyExtractor;
    private Assembler<S, D> assembler;

    public ListAssembler(KeyExtractor<S, K> srcKeyExtractor, KeyExtractor<D, K> dstKeyExtractor, Assembler<S, D> assembler) {
        this.srcKeyExtractor = srcKeyExtractor;
        this.dstKeyExtractor = dstKeyExtractor;
        this.assembler = assembler;
    }

    @Override
    public void accept(List<S> sources, List<D> destinations) {
        final Map<K, S> srcKeyMap = sources.stream().collect(Collectors.toMap(srcKeyExtractor, src -> src));
        for (D dst : destinations) {
            final K key = dstKeyExtractor.apply(dst);
            if (key != null && srcKeyMap.containsKey(key)) {
                assembler.accept(srcKeyMap.get(key), dst);
            }
        }
    }

}
