package edu.whu.ioc.parser;

import edu.whu.ioc.config.BeanConstructorArg;
import edu.whu.ioc.config.BeanDefinition;
import edu.whu.ioc.config.BeanProperty;
import edu.whu.ioc.exception.BeanParseException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class XMLBeanParserTest {

    @Test
    void testParse() throws BeanParseException {
        Map<String,BeanDefinition> beanDefMap = XMLBeanParser.parse("bean-creation.xml");
        assertEquals(5,beanDefMap.size());

        BeanDefinition bookDaoBean=beanDefMap.get("bookDao");
        assertNotNull(bookDaoBean.getBeanClass());
        assertEquals("com.itheima.dao.impl.BookDaoImpl",bookDaoBean.getBeanClass().getName());

        BeanDefinition orderDaoBean = beanDefMap.get("orderDao");
        assertEquals("getOrderDao",orderDaoBean.getFactoryMethodName());

        BeanDefinition userDao = beanDefMap.get("userDao");
        assertEquals("getUserDao",userDao.getFactoryMethodName());
        assertEquals("userFactory",userDao.getFactoryBeanName());
    }


    @Test
    void testParsePropDI() throws BeanParseException {
        Map<String, BeanDefinition> beanDefMap = XMLBeanParser.parse("bean-prop-di.xml");
        assertEquals(3, beanDefMap.size());
        BeanDefinition bookDao = beanDefMap.get("bookDao");
        List<BeanProperty> props = bookDao.getBeanProperties();
        assertEquals(2, props.size());
        assertEquals("connectionNum", props.get(0).getName());
        assertEquals("100", props.get(0).getValue());
        assertEquals("databaseName", props.get(1).getName());
        assertEquals("mysql", props.get(1).getValue());

        BeanDefinition bookService = beanDefMap.get("bookService");
        props = bookService.getBeanProperties();
        assertEquals(2, props.size());
        assertEquals("userDao", props.get(0).getName());
        assertEquals("userDao", props.get(0).getRef());
        assertEquals("bookDao", props.get(1).getName());
        assertEquals("bookDao", props.get(1).getRef());

    }

    @Test
    void testParseContructorDI() throws BeanParseException {
        Map<String, BeanDefinition> beanDefMap = XMLBeanParser.parse("bean-constructor-di.xml");
        assertEquals(3, beanDefMap.size());
        BeanDefinition bookDao = beanDefMap.get("bookDao");
        List<BeanConstructorArg> args = bookDao.getConstructorArgs();
        assertEquals(0, args.get(0).getIndex());
        assertEquals(String.class, args.get(0).getType());
        assertEquals("mysql",  args.get(0).getValue());

        BeanDefinition bookService = beanDefMap.get("bookService");
        args = bookService.getConstructorArgs();
        assertEquals(0, args.get(0).getIndex());
        assertEquals("bookDao",  args.get(0).getRef());
    }

    }