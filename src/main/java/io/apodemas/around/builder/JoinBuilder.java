package io.apodemas.around.builder;

import io.apodemas.around.engine.TypedResource;
import io.apodemas.around.engine.com.KeyExtractor;
import io.apodemas.around.engine.com.ListFetcher;
import io.apodemas.around.engine.rule.JoinRule;

/**
 * @author: Cao Zheng
 * @date: 2023/7/30
 * @description:
 */
public class JoinBuilder<R, S, D> {
    private TypedResource<S> source;
    private TypedResource<D> dest;
    private Context<R> ctx;

    JoinBuilder(TypedResource<S> source, TypedResource<D> dest, Context<R> ctx) {
        this.source = source;
        this.dest = dest;
        this.ctx = ctx;
    }

    public <K> ResourceBuilder<R, D> on(KeyExtractor<S, K> sourceKeyExtractor, ListFetcher<K, D> fetcher) {
        final JoinRule<S, D, K> rule = new JoinRule<>();
        rule.source(source);
        rule.sourceKeyExtractor(sourceKeyExtractor);
        rule.dest(dest);
        rule.fetcher(fetcher);
        ctx.rules().add(rule);

        return new ResourceBuilder(dest, ctx);
    }

}
