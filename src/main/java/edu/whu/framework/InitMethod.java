package edu.whu.framework;

import java.lang.annotation.*;

/**
 * 标注在方法上，指示该方法需要在启动时调用
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InitMethod {

}