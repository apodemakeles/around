package io.apodemas.around.engine;

/**
 * @author: Cao Zheng
 * @date: 2023/6/17
 * @description:
 */
public class RuleInvalidException extends RuntimeException {
    public RuleInvalidException(String message) {
        super(message);
    }
}
