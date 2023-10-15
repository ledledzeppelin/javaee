package edu.whu.ioc;

import edu.whu.ioc.config.BeanDefinition;
import edu.whu.ioc.exception.BeanCreationException;
import edu.whu.ioc.exception.BeanParseException;
import edu.whu.ioc.parser.XMLBeanParser;

import java.util.Map;

/**
 * 支持XML配置的Mini IOC实现。继承AbstractMiniApplicationContext来实现Bean的创建依赖注入和初始化。
 */
public class XMLMiniApplicationContext extends AbstractMiniApplicationContext{

    public XMLMiniApplicationContext(String beanXML) throws BeanParseException, BeanCreationException {
        Map<String, BeanDefinition> result = XMLBeanParser.parse(beanXML);
        this.beanDefinitionMap.putAll(result);
        createAndInitBeans();
    }

}
