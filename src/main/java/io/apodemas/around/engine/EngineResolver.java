package io.apodemas.around.engine;

import io.apodemas.around.engine.com.ListKeyExtractor;
import io.apodemas.around.engine.task.TaskExecutor;

import java.util.Objects;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class EngineResolver {

    public <S> Engine<S> resolve(Rules<S> rules) {
        // 检查


        for (JoinRule<?, ?, ?> join : rules.getJoins()) {
        }
    }


    private static class JoinKey {
        private Class<?> leftClazz;
        private Class<?> rightClazz;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JoinKey joinKey = (JoinKey) o;
            return Objects.equals(leftClazz, joinKey.leftClazz) && Objects.equals(rightClazz, joinKey.rightClazz);
        }

        @Override
        public int hashCode() {
            return Objects.hash(leftClazz, rightClazz);
        }
    }

    private static class Join {
        private ListKeyExtractor keysExtractor;
        private TaskExecutor<Resource> getKeysExecutor;
        private TaskExecutor<Resource> fetchExecutor;
    }
}
