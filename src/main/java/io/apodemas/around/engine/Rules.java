package io.apodemas.around.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class Rules<S> {
    private Resource<S> source;
    private List<JoinRule<?, ?, ?>> joins = new ArrayList<>();

    public Resource<S> getSource() {
        return source;
    }

    public void setSource(Resource<S> source) {
        this.source = source;
    }

    public List<JoinRule<?, ?, ?>> getJoins() {
        return joins;
    }

    public void addJoinRule(JoinRule<?, ?, ?> joinRule) {
        joins.add(joinRule);
    }
}
