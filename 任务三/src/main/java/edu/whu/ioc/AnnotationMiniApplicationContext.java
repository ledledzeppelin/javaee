package edu.whu.ioc;

import edu.whu.ioc.config.BeanDefinition;
import edu.whu.ioc.exception.BeanCreationException;
import edu.whu.ioc.exception.BeanParseException;
import edu.whu.ioc.parser.AnnotatedBeanParser;
import edu.whu.ioc.parser.XMLBeanParser;

import java.util.Map;

/**
 * 注解模式的MiniApplicationContext
 */
public class AnnotationMiniApplicationContext extends AbstractMiniApplicationContext{

    public AnnotationMiniApplicationContext(Class<?> configClass) throws BeanParseException, BeanCreationException {
        Map<String, BeanDefinition> result = AnnotatedBeanParser.parse(configClass);
        this.beanDefinitionMap.putAll(result);
        createAndInitBeans();
    }
}
