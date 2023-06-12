package io.apodemas.around.engine;

import io.apodemas.around.engine.com.ListKeyExtractor;
import io.apodemas.around.engine.task.TaskExecutor;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Objects;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class RuleResolver {

    public <S> Engine<S> resolve(Rules<S> rules) {
        // 检查

        // 构建alias

        // 合并

        for (JoinRule<?, ?, ?> join : rules.getJoins()) {

        }

        throw new NotImplementedException();
    }


}
