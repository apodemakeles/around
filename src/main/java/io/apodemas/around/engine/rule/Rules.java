package io.apodemas.around.engine.rule;

import io.apodemas.around.engine.TypedResource;
import io.apodemas.around.engine.rule.Rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        // todo: 类型检查

        final Set<TypedResource> sourceSet = new HashSet<>();
        sourceSet.add(root);
        final Set<TypedResource> dependencySet = new HashSet<>();
        boolean hasAssembler = false;
        for (Rule rule : items) {
            if (rule instanceof AssembleRule) {
                final AssembleRule assembleRule = (AssembleRule) rule;
                if (!assembleRule.root().equals(root)) {
                    throw new RuleInvalidException(String.format("%s can not be root of assembler", assembleRule.root().name()));
                }
                dependencySet.add(assembleRule.source());
                hasAssembler = true;
            } else if (rule instanceof JoinRule) {
                final JoinRule joinRule = (JoinRule) rule;
                sourceSet.add(joinRule.dest());
                dependencySet.add(joinRule.source());
            }
        }
        for (TypedResource res : dependencySet) {
            if (!sourceSet.contains(res)) {
                throw new RuleInvalidException("lack of source of " + res.name());
            }
        }

        if (!hasAssembler) {
            throw new RuleInvalidException("need one at least assembler");
        }
    }
}
