package io.apodemas.around.common.lambda;

import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author: Cao Zheng
 * @date: 2023/8/9
 * @description:
 */
public class LambdaResolverTest {

    @Test
    public void runnable_lambda_should_work() throws Exception {
        Serializable lambda = (Runnable & Serializable) () -> {
            System.out.println("Hello, world!");
        };
        final LambdaInfo info = LambdaResolver.getInfo(lambda);
        Assert.assertEquals(LambdaResolverTest.class, info.getImplClass());
        Assert.assertEquals(0, info.getParameterTypes().length);
        Assert.assertEquals(void.class, info.getReturnType());
    }

    @Test
    public void consumer_lambda_should_work() throws Exception {
        Serializable lambda = (Consumer<Long> & Serializable) (Long number) -> {
            System.out.println("Hello," + number);
        };
        final LambdaInfo info = LambdaResolver.getInfo(lambda);
        Assert.assertEquals(LambdaResolverTest.class, info.getImplClass());
        Assert.assertEquals(1, info.getParameterTypes().length);
        Assert.assertEquals(Long.class, info.getParameterTypes()[0]);
        Assert.assertEquals(void.class, info.getReturnType());
    }

    @Test
    public void supplier_lambda_should_work() throws Exception {
        Serializable lambda = (Supplier<MockObject> & Serializable) () -> {
            return new MockObject();
        };
        final LambdaInfo info = LambdaResolver.getInfo(lambda);
        Assert.assertEquals(LambdaResolverTest.class, info.getImplClass());
        Assert.assertEquals(0, info.getParameterTypes().length);
        Assert.assertEquals(MockObject.class, info.getReturnType());
    }

    @Test
    public void function_lambda_should_work() throws Exception {
        final Serializable lambda = (Function<String, Object> & Serializable) (String name) -> {
            return new Object();
        };
        final LambdaInfo info = LambdaResolver.getInfo(lambda);
        Assert.assertEquals(LambdaResolverTest.class, info.getImplClass());
        Assert.assertEquals(1, info.getParameterTypes().length);
        Assert.assertEquals(String.class, info.getParameterTypes()[0]);
        Assert.assertEquals(Object.class, info.getReturnType());
    }

    @Test
    public void bi_function_lambda_should_work() throws Exception {
        final Serializable lambda = (BiFunction<Boolean, MockGenericObject<String>, Void> & Serializable) (Boolean ok, MockGenericObject<String> object) -> {
            return null;
        };
        final LambdaInfo info = LambdaResolver.getInfo(lambda);
        Assert.assertEquals(LambdaResolverTest.class, info.getImplClass());
        Assert.assertEquals(2, info.getParameterTypes().length);
        Assert.assertEquals(Boolean.class, info.getParameterTypes()[0]);
        Assert.assertEquals(MockGenericObject.class, info.getParameterTypes()[1]);
        Assert.assertEquals(Void.class, info.getReturnType());
    }

    @Test
    public void s_function_method_should_work() throws Exception {
        final MockFunction function = new MockFunction();
        SFunction<String, List<String>> fn = function::acc;
        final LambdaInfo info = LambdaResolver.getInfo(fn);
        Assert.assertEquals(MockFunction.class, info.getImplClass());
        Assert.assertEquals(1, info.getParameterTypes().length);
        Assert.assertEquals(String.class, info.getParameterTypes()[0]);
        Assert.assertEquals(List.class, info.getReturnType());
    }

    @Test
    public void s_bi_consumer_should_work() throws Exception {
        final MockBiConsumer consumer = new MockBiConsumer();
        SBiConsumer<Long, String> fn = consumer::acc;
        final LambdaInfo info = LambdaResolver.getInfo(fn);
        Assert.assertEquals(MockBiConsumer.class, info.getImplClass());
        Assert.assertEquals(2, info.getParameterTypes().length);
        Assert.assertEquals(Long.class, info.getParameterTypes()[0]);
        Assert.assertEquals(String.class, info.getParameterTypes()[1]);
    }

    @Test(expected = RuntimeException.class)
    public void non_lambda_should_throw_exception() throws Exception {
        final LambdaInfo info = LambdaResolver.getInfo(new NotLambda());
    }

    public static class MockObject {

    }

    public static class MockGenericObject<T> {

    }

    public static class MockFunction {
        public List<String> acc(String name) {
            return new ArrayList<>();
        }
    }

    public static class MockBiConsumer {
        public void acc(Long number, String name) {

        }
    }

    public static class NotLambda implements Serializable {
    }
}
