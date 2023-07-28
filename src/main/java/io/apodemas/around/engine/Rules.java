package io.apodemas.around.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class Rules<S> {
    private TypedResource<S> source;
    private List<JoinRule<?, ?, ?>> joins = new ArrayList<>();

    public TypedResource<S> getSource() {
        return source;
    }

    public void setSource(TypedResource<S> source) {
        this.source = source;
    }

    public List<JoinRule<?, ?, ?>> getJoins() {
        return joins;
    }

    public void addJoinRule(JoinRule<?, ?, ?> joinRule) {
        joins.add(joinRule);
    }
}
