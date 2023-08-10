package io.apodemas.around.engine.com;

import java.io.Serializable;
import java.util.function.BiConsumer;

/**
 * @author: Cao Zheng
 * @date: 2023/7/28
 * @description:
 */
@FunctionalInterface
public interface Assembler<S, D> extends BiConsumer<S, D>, Serializable {
}
