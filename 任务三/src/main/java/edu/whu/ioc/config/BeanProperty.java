package edu.whu.ioc.config;

import lombok.Data;

/**
 * Bean的属性定义
 */
@Data
public class BeanProperty {

    /**
     * 属性名
     */
    String name;
    /**
     * 属性值
     */
    String value;
    /**
     * 属性引用
     */
    String ref;

}
