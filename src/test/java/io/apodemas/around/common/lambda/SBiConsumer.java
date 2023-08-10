package io.apodemas.around.common.lambda;

import java.io.Serializable;
import java.util.function.BiConsumer;

/**
 * @author: Cao Zheng
 * @date: 2023/8/10
 * @description:
 */
@FunctionalInterface
public interface SBiConsumer<T, K> extends BiConsumer<T, K>, Serializable {
}
