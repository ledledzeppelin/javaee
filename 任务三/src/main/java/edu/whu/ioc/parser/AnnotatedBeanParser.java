package edu.whu.ioc.parser;

import edu.whu.ioc.config.BeanDefinition;
import edu.whu.ioc.exception.BeanParseException;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring注解的解析器（未完成）
 */
public class AnnotatedBeanParser {

    public static Map<String, BeanDefinition> parse(Class<?> configClass) throws BeanParseException {
        Map<String, BeanDefinition> beanDefinitions = new HashMap();
        //TODO
        return beanDefinitions;
    }
}
