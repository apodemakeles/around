package io.apodemas.around.engine;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class RuleResolver {

    private EngineSettings settings;

    public RuleResolver(EngineSettings settings) {
        this.settings = settings;
    }

    public <S> Engine<S> resolve(Rules<S> rules) {
        // 检查alias
        // 检查fillFn是否合法

        // 合并

        for (JoinRule<?, ?, ?> join : rules.getJoins()) {

        }

        throw new NotImplementedException();
    }


}
