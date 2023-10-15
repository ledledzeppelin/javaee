package edu.whu.ioc;

import com.itheima.dao.BookDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.UserDao;
import com.itheima.dao.impl.BookDaoImpl;
import com.itheima.service.BookService;
import com.itheima.service.impl.BookServiceImpl;
import edu.whu.ioc.exception.BeanCreationException;
import edu.whu.ioc.exception.BeanParseException;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.*;


public class XMLMiniApplicationContextTest {

    @Test
    public void testBeanCreation() throws BeanCreationException, BeanParseException {
       MiniApplicationContext context= new XMLMiniApplicationContext("bean-creation.xml");
       assertNotNull((BookDao)context.getBean("bookDao"));
       assertNotNull((UserDao)context.getBean("userDao"));
       assertNotNull((OrderDao)context.getBean("orderDao"));
       assertNotNull(context.getBean("bookDao", BookDao.class));
       assertNotNull(context.getBean(BookDao.class));
        //TODO 需要测试更多异常情况
    }

    @Test
    public void testPropertyDI() throws BeanCreationException, BeanParseException {
        MiniApplicationContext context= new XMLMiniApplicationContext("bean-prop-di.xml");
        BookDaoImpl bookDao = (BookDaoImpl) context.getBean("bookDao");
        assertNotNull(bookDao);
        assertEquals(100,bookDao.getConnectionNum());
        assertEquals("mysql",bookDao.getDatabaseName());

        BookServiceImpl bookService=context.getBean("bookService",BookServiceImpl.class);
        assertNotNull(bookService);
        assertNotNull(bookService.getBookDao());
        assertNotNull(bookService.getUserDao());
        //TODO 需要测试更多异常情况
    }


    @Test
    public void testConstructorDI() throws BeanCreationException, BeanParseException {
        MiniApplicationContext context= new XMLMiniApplicationContext("bean-constructor-di.xml");
        BookDaoImpl bookDao = (BookDaoImpl) context.getBean("bookDao");
        assertNotNull(bookDao);
        assertEquals(100,bookDao.getConnectionNum());
        assertEquals("mysql",bookDao.getDatabaseName());

        BookServiceImpl bookService= context.getBean("bookService",BookServiceImpl.class);
        assertNotNull(bookService);
        assertNotNull(bookService.getBookDao());
        assertNotNull(bookService.getUserDao());
        //TODO 需要测试异常情况。
    }

    @Test
    public void testSpring(){
        ApplicationContext ctx1=new ClassPathXmlApplicationContext("bean-constructor-di.xml");
        BookDaoImpl bookDao = (BookDaoImpl) ctx1.getBean("bookDao");
        ctx1=new ClassPathXmlApplicationContext("bean-prop-di.xml");
    }


}