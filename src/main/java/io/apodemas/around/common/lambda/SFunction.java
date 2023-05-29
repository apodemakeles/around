package io.apodemas.around.common.lambda;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
@FunctionalInterface
public interface SFunction<T, K> extends Function<T, K>, Serializable {
    K apply(T t);

    default String getMethodName() {
        try {
            Method method = getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(this);
            return serializedLambda.getImplMethodName();
        } catch (Exception e) {
            throw new RuntimeException("fail to resolve method name from SFunction", e);
        }
    }
}
