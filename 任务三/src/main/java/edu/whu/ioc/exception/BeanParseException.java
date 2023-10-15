package edu.whu.ioc.exception;

/**
 * 解析BeanDefinition时产生的异常
 */
public class BeanParseException extends Exception{
    public BeanParseException(String message) {
        super(message);
    }
}
