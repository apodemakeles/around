package io.apodemas.around.engine.rule;

import io.apodemas.around.engine.TypedResource;
import io.apodemas.around.engine.rule.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class Rules<S> {
    private TypedResource<S> root;
    private List<Rule> items = new ArrayList<>();

    public TypedResource<S> root() {
        return root;
    }

    public void root(TypedResource<S> root) {
        this.root = root;
    }

    public void add(Rule rule) {
        rule.check();
        items.add(rule);
    }

    public List<Rule> items() {
        return items;
    }

    public void check() {
        if (root == null) {
            throw new RuleInvalidException("missing root");
        }
    }
}
