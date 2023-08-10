package io.apodemas.around.common.lambda;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author: Cao Zheng
 * @date: 2023/8/9
 * @description:
 */
public class LambdaResolver {

    /**
     * 解析可序列化的lambda表达式
     *
     * @param fn
     * @return
     * @throws ClassNotFoundException
     */
    public static LambdaInfo getInfo(Serializable fn) throws ClassNotFoundException {
        final LambdaInfo info = new LambdaInfo();
        final SerializedLambda serializedLambda = getSerializedLambda(fn);
        info.setImplClass(getImplClass(serializedLambda.getImplClass()));
        info.setParameterTypes(getParamTypes(serializedLambda.getImplMethodSignature()));
        info.setReturnType(getReturnType(serializedLambda.getImplMethodSignature()));

        return info;
    }

    private static SerializedLambda getSerializedLambda(Serializable fn) {
        try {
            final Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);

            return (SerializedLambda) method.invoke(fn);
        } catch (Exception e) {
            throw new RuntimeException("could not get SerializedLambda", e);
        }
    }

    private static Class<?> getImplClass(String classDescriptor) throws ClassNotFoundException {
        final String className = classDescriptor.replace("/", ".");
        return Class.forName(className);
    }

    private static Class<?>[] getParamTypes(String methodSignature) throws ClassNotFoundException {
        String[] descriptors = methodSignature.substring(1, methodSignature.lastIndexOf(')')).split(";");
        if (descriptors.length == 1 && Objects.equals(descriptors[0], "")) {
            descriptors = new String[0];
        }
        Class<?>[] params = new Class[descriptors.length];
        for (int i = 0; i < descriptors.length; i++) {
            params[i] = descriptorToClass(descriptors[i] + ";");
        }

        return params;
    }

    private static Class<?> getReturnType(String methodSignature) throws ClassNotFoundException {
        return descriptorToClass(methodSignature.substring(methodSignature.lastIndexOf(')') + 1));
    }

    private static Class<?> descriptorToClass(String descriptor) throws ClassNotFoundException {
        switch (descriptor.charAt(0)) {
            case 'V':
                return void.class;
            case 'Z':
                return boolean.class;
            case 'B':
                return byte.class;
            case 'S':
                return short.class;
            case 'I':
                return int.class;
            case 'J':
                return long.class;
            case 'F':
                return float.class;
            case 'D':
                return double.class;
            case 'C':
                return char.class;
            case 'L':
                // 对象类型，去掉开头的 'L' 和结尾的 ';'
                String className = descriptor.substring(1, descriptor.length() - 1).replace('/', '.');
                return Class.forName(className);
            case '[':
                // 数组类型，递归调用
                Class<?> componentClass = descriptorToClass(descriptor.substring(1));
                return Array.newInstance(componentClass, 0).getClass();
            default:
                throw new ClassNotFoundException("Unsupported descriptor: " + descriptor);
        }
    }
}
