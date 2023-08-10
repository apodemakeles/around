package io.apodemas.around.builder;

import io.apodemas.around.engine.TypedResource;
import io.apodemas.around.engine.rule.Rules;

/**
 * @author: Cao Zheng
 * @date: 2023/7/30
 * @description:
 */
class Context<R> {
    private TypedResource<R> root;
    private Rules<R> rules;

    public Context(TypedResource<R> root) {
        this.root = root;
        this.rules = new Rules<>();
        this.rules.root(root);
    }

    public TypedResource<R> root() {
        return root;
    }

    public Rules<R> rules() {
        return rules;
    }
}
