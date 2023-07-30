package io.apodemas.around.engine.rule;

import io.apodemas.around.engine.TypedResource;
import io.apodemas.around.engine.com.KeyExtractor;
import io.apodemas.around.engine.com.ListFetcher;

/**
 * @author: Cao Zheng
 * @date: 2023/7/30
 * @description:
 */
public class JoinRule<S, D, K> implements Rule {
    private TypedResource<S> source;
    private TypedResource<D> dest;
    private KeyExtractor<S, K> sourceKeyExtractor;
    private ListFetcher<K, D> fetcher;

    public TypedResource<S> source() {
        return source;
    }

    public void source(TypedResource<S> source) {
        this.source = source;
    }

    public TypedResource<D> dest() {
        return dest;
    }

    public void dest(TypedResource<D> dest) {
        this.dest = dest;
    }

    public KeyExtractor<S, K> sourceKeyExtractor() {
        return sourceKeyExtractor;
    }

    public void sourceKeyExtractor(KeyExtractor<S, K> sourceKeyExtractor) {
        this.sourceKeyExtractor = sourceKeyExtractor;
    }

    public ListFetcher<K, D> fetcher() {
        return fetcher;
    }

    public void fetcher(ListFetcher<K, D> fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public void check() {
        if (source == null) {
            throw new RuleInvalidException("missing source");
        }
        if (dest == null) {
            throw new RuleInvalidException("missing dest");
        }
        if (sourceKeyExtractor == null) {
            throw new RuleInvalidException("missing sourceKeyExtractor");
        }
        if (fetcher == null) {
            throw new RuleInvalidException("missing fetcher");
        }
    }
}
