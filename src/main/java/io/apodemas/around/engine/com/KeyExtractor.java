package io.apodemas.around.engine.com;

import java.util.function.Function;

/**
 * @author: Cao Zheng
 * @date: 2023/7/29
 * @description:
 */
@FunctionalInterface
public interface KeyExtractor<S, K> extends Function<S, K> {
}
