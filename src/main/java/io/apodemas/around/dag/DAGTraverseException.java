package io.apodemas.around.dag;

/**
 * @author: Cao Zheng
 * @date: 2023/5/25
 * @description:
 */
public class DAGTraverseException extends RuntimeException {
    public DAGTraverseException(String message) {
        super(message);
    }

    public DAGTraverseException(String message, Throwable cause) {
        super(message, cause);
    }
}
