package edu.whu.ioc.parser;

import edu.whu.ioc.config.BeanConstructorArg;
import edu.whu.ioc.config.BeanDefinition;
import edu.whu.ioc.config.BeanProperty;
import edu.whu.ioc.exception.BeanParseException;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对Spring的XML配置文件的解析工具
 */
public class XMLBeanParser {


    /**
     * 解析Classpath中的XML文件，得到BeanDefinition的Map
     * @param xmlInClasspath
     * @return
     * @throws BeanParseException
     */
    public static Map<String, BeanDefinition> parse(String xmlInClasspath) throws BeanParseException {
        Map<String, BeanDefinition> beanDefinitions = new HashMap();
        Document document = loadBeanXML(xmlInClasspath);
        List<Node> list = document.selectNodes("b:beans/b:bean");
        for (Node node : list) {
            if (!(node instanceof Element)) {
                throw new BeanParseException("malformed bean config file.");
            }
            BeanDefinition beanDef = parseBean((Element) node);
            beanDefinitions.put(beanDef.getBeanName(), beanDef);
        }
        BeanDefValidator.validate(beanDefinitions);
        return beanDefinitions;
    }

    /**
     * 加载XML配置文件
     * @param xmlInClasspath
     * @return
     * @throws BeanParseException
     */
    private static Document loadBeanXML(String xmlInClasspath) throws BeanParseException {
        try {
            InputStream inputStream = XMLBeanParser.class.getClassLoader().getResourceAsStream(xmlInClasspath);
            SAXReader saxReader = new SAXReader();
            //Spring 的XML配置文件中标签都有命名空间约束，因此将命名空间到saxReader中，以便于解析
            Map<String, String> map = new HashMap<String, String>();
            map.put("b", "http://www.springframework.org/schema/beans");
            saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
            return saxReader.read(inputStream);
        } catch (Exception ex) {
            throw new BeanParseException("Cannot load the bean config file '" + xmlInClasspath + "'");
        }
    }


    /**
     * 解析XML中的Bean
     * @param beanElement
     * @return
     * @throws BeanParseException
     */
    private static BeanDefinition parseBean(Element beanElement) throws BeanParseException {
        BeanDefinition beanDefinition = new BeanDefinition();
        parseBeanBaseInfo(beanElement, beanDefinition);
        for (Element element : beanElement.elements()) {
            if("property".equals(element.getName())){
                BeanProperty prop= parseBeanProperty(element);
                beanDefinition.getBeanProperties().add(prop);
            }else if("constructor-arg".equals(element.getName())){
                BeanConstructorArg arg=parsConstructorArg(element);
                beanDefinition.getConstructorArgs().add(arg);
            }
        }
        return beanDefinition;
    }

    /**
     * 解析Bean的构造函数参数定义
     * @param element
     * @return
     * @throws BeanParseException
     */
    private static BeanConstructorArg parsConstructorArg(Element element) throws BeanParseException {
        BeanConstructorArg constructorArg=new BeanConstructorArg();
        for (Attribute attribute : element.attributes()) {
            if ("index".equals(attribute.getName())) {
                constructorArg.setIndex(Integer.parseInt(attribute.getValue()));
            }else if ("name".equals(attribute.getName())) {
                constructorArg.setName(attribute.getValue());
            }else if ("type".equals(attribute.getName())) {
                try {
                    String type=attribute.getValue();
                    Class<?> clazz=null;
                    if("int".equals(type)){
                        clazz=int.class;
                    }else if("float".equals(type)){
                        clazz=float.class;
                    }else if("double".equals(type)){
                        clazz=double.class;
                    }else{
                        clazz = Class.forName(type);
                    }
                    constructorArg.setType(clazz);
                } catch (ClassNotFoundException e) {
                    throw new BeanParseException("class not found:" + e.getMessage());
                }
            }else if ("value".equals(attribute.getName())) {
                constructorArg.setValue(attribute.getValue());
            }else if ("ref".equals(attribute.getName())) {
                constructorArg.setRef(attribute.getValue());
            }
        }
        return constructorArg;
    }

    /**
     * 解析Bean的属性定义
     * @param element
     * @return
     */
    private static BeanProperty parseBeanProperty(Element element) {
        BeanProperty property=new BeanProperty();
        for (Attribute attribute : element.attributes()) {
            if ("name".equals(attribute.getName())) {
                property.setName(attribute.getValue());
            } else if ("value".equals(attribute.getName())) {
                property.setValue(attribute.getValue());
            } else if ("ref".equals(attribute.getName())) {
                property.setRef(attribute.getValue());
            }
        }
        return property;
    }


    /**
     * 解析Bean的基本信息
     * @param beanElement
     * @param beanDefinition
     * @throws BeanParseException
     */
    private static void parseBeanBaseInfo(Element beanElement, BeanDefinition beanDefinition) throws BeanParseException {
        for (Attribute attribute : beanElement.attributes()) {
            if ("id".equals(attribute.getName())) {
                beanDefinition.setBeanName(attribute.getValue());
            } else if ("class".equals(attribute.getName())) {
                try {
                    Class<?> clazz = Class.forName(attribute.getValue());
                    beanDefinition.setBeanClass(clazz);
                } catch (ClassNotFoundException e) {
                    throw new BeanParseException("class not found:" + e.getMessage());
                }
            } else if ("factory-method".equals(attribute.getName())) {
                beanDefinition.setFactoryMethodName(attribute.getValue());
            } else if ("factory-bean".equals(attribute.getName())) {
                beanDefinition.setFactoryBeanName(attribute.getValue());
            } else if ("init-method".equals(attribute.getName())) {
                beanDefinition.setInitMethodName(attribute.getValue());
            } else if ("autowire".equals(attribute.getName())) {
                if ("byName".equals(attribute.getValue())) {
                    beanDefinition.setAutowireMode(BeanDefinition.Autowire.BYNAME);
                } else if ("byType".equals(attribute.getValue())) {
                    beanDefinition.setAutowireMode(BeanDefinition.Autowire.BYTYPE);
                }
            } else if ("scope".equals(attribute.getName())) {
                if ("singleton".equals(attribute.getValue())) {
                    beanDefinition.setScope(BeanDefinition.Scope.SINGLETON);
                } else if ("prototype".equals(attribute.getValue())) {
                    beanDefinition.setScope(BeanDefinition.Scope.PROTOTYPE);
                }
            }
        }
    }


}
