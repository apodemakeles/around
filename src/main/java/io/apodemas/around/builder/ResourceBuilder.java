package io.apodemas.around.builder;

import io.apodemas.around.engine.Engine;
import io.apodemas.around.engine.EngineSettings;
import io.apodemas.around.engine.RuleResolver;
import io.apodemas.around.engine.TypedResource;
import io.apodemas.around.engine.com.Assembler;
import io.apodemas.around.engine.rule.AssembleRule;

/**
 * @author: Cao Zheng
 * @date: 2023/8/5
 * @description:
 */
class ResourceBuilder<R, S> {
    private TypedResource<S> resource;
    private Context<R> ctx;

    ResourceBuilder(TypedResource<S> resource, Context<R> ctx) {
        this.resource = resource;
        this.ctx = ctx;
    }

    public <D> JoinBuilder<R, S, D> join(Class<D> type) {
        return join(resource, new TypedResource<>(type));
    }

    public <D> JoinBuilder<R, S, D> join(Class<D> type, String alias) {
        return join(resource, new TypedResource<>(type, alias));
    }

    private <SS, D> JoinBuilder<R, SS, D> join(TypedResource<SS> source, TypedResource<D> dest) {
        return new JoinBuilder<>(source, dest, ctx);
    }

    public void assemble() {

    }

    public Engine<R> build(EngineSettings settings) {
        final RuleResolver resolver = new RuleResolver(settings);
        return resolver.resolve(ctx.rules());
    }
}
