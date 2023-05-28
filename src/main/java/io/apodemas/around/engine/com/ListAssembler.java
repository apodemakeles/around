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
    private Function<S, K> srcKeyExtractor;
    private List<Item<S, D, K>> items = new ArrayList<>();

    public ListAssembler(Function<S, K> srcKeyExtractor) {
        this.srcKeyExtractor = srcKeyExtractor;
    }

    public void add(Function<D, K> dstKeyExtractor, BiConsumer<S, D> assembler) {
        Item<S, D, K> item = new Item<>();
        item.dstKeyExtractor = dstKeyExtractor;
        item.assembler = assembler;
        items.add(item);
    }

    @Override
    public void accept(List<S> sources, List<D> destinations) {
        final Map<K, S> srcKeyMap = sources.stream().collect(Collectors.toMap(srcKeyExtractor, src -> src));
        for (D dst : destinations) {
            for (Item<S, D, K> item : items) {
                final K key = item.dstKeyExtractor.apply(dst);
                if (key != null && srcKeyMap.containsKey(key)) {
                    item.assembler.accept(srcKeyMap.get(key), dst);
                }
            }
        }
    }

    private static class Item<S, D, K> {
        private Function<D, K> dstKeyExtractor;
        private BiConsumer<S, D> assembler;
    }

}
