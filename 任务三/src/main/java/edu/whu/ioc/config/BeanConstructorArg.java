package edu.whu.ioc.config;

import lombok.Data;

/**
 * 构造函数的参数定义
 */
@Data
public class BeanConstructorArg {

    /**
     * 参数序号
     */
    Integer index;

    /**
     * 参数名（一遍情况下编译后无法识别参数名）
     */
    String name;

    /**
     * 参数类型
     */
    Class<?> type;

    /**
     * 参数值
     */
    String value;

    /**
     * 参数引用的Bean
     */
    String ref;

}
