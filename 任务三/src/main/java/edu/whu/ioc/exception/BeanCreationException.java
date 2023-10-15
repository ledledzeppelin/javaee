package edu.whu.ioc.exception;

/**
 * Bean创建实例、注入依赖和调用初始方法时的异常
 */
public class BeanCreationException extends Exception{

    public BeanCreationException(String message) {
        super(message);
    }
}
