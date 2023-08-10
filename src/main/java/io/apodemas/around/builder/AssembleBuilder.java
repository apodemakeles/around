package io.apodemas.around.builder;

import io.apodemas.around.engine.TypedResource;

/**
 * @author: Cao Zheng
 * @date: 2023/7/30
 * @description:
 */
public class AssembleBuilder<R, S> {
    private TypedResource<S> source;
    private Context<R> ctx;

    public AssembleBuilder(TypedResource<S> source, Context<R> ctx) {
        this.source = source;
        this.ctx = ctx;
    }

    
}
