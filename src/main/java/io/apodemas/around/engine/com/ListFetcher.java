package io.apodemas.around.engine.com;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * @author: Cao Zheng
 * @date: 2023/7/29
 * @description:
 */
@FunctionalInterface
public interface ListFetcher<K, R> extends Function<List<K>, List<R>>, Serializable {
}
