package io.apodemas.around.common.lambda;

/**
 * @author: Cao Zheng
 * @date: 2023/8/9
 * @description:
 */

public class LambdaInfo {
    private Class<?> implClass;
    private Class<?>[] parameterTypes;
    private Class<?> returnType;

    public Class<?> getImplClass() {
        return implClass;
    }

    public void setImplClass(Class<?> implClass) {
        this.implClass = implClass;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }
}
