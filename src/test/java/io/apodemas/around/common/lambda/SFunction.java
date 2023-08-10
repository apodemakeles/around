package io.apodemas.around.common.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author: Cao Zheng
 * @date: 2023/8/10
 * @description:
 */
@FunctionalInterface
public interface SFunction<T, K> extends Function<T, K>, Serializable {
}
