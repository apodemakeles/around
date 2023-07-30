package io.apodemas.around.engine.rule;

import io.apodemas.around.engine.TypedResource;
import io.apodemas.around.engine.com.Assembler;
import io.apodemas.around.engine.com.KeyExtractor;

/**
 * @author: Cao Zheng
 * @date: 2023/7/30
 * @description:
 */
public class AssembleRule<R, S, K> implements Rule {
    private TypedResource<R> root;
    private TypedResource<S> source;
    private KeyExtractor<R, K> rootKeyExtractor;
    private KeyExtractor<S, K> sourceKeyExtractor;
    private Assembler<S, R> assembler;

    public TypedResource<R> root() {
        return root;
    }

    public void root(TypedResource<R> root) {
        this.root = root;
    }

    public TypedResource<S> source() {
        return source;
    }

    public void source(TypedResource<S> source) {
        this.source = source;
    }

    public KeyExtractor<R, K> rootKeyExtractor() {
        return rootKeyExtractor;
    }

    public void rootKeyExtractor(KeyExtractor<R, K> rootKeyExtractor) {
        this.rootKeyExtractor = rootKeyExtractor;
    }

    public KeyExtractor<S, K> sourceKeyExtractor() {
        return sourceKeyExtractor;
    }

    public void sourceKeyExtractor(KeyExtractor<S, K> sourceKeyExtractor) {
        this.sourceKeyExtractor = sourceKeyExtractor;
    }

    public Assembler<S, R> assembler() {
        return assembler;
    }

    public void assembler(Assembler<S, R> assembler) {
        this.assembler = assembler;
    }

    @Override
    public void check() {
        if (root == null) {
            throw new RuleInvalidException("missing root");
        }
        if (source == null) {
            throw new RuleInvalidException("missing source");
        }
        if (rootKeyExtractor == null) {
            throw new RuleInvalidException("missing rootKeyExtractor");
        }
        if (sourceKeyExtractor == null) {
            throw new RuleInvalidException("missing sourceKeyExtractor");
        }
        if (assembler == null) {
            throw new RuleInvalidException("missing assembler");
        }
    }
}
