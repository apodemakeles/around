package io.apodemas.around.common;

/**
 * @author: Cao Zheng
 * @date: 2023/7/29
 * @description:
 */
public abstract class Assert {

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
