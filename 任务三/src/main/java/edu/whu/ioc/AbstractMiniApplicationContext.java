package edu.whu.ioc;

import edu.whu.ioc.config.BeanConstructorArg;
import edu.whu.ioc.config.BeanDefinition;
import edu.whu.ioc.config.BeanProperty;
import edu.whu.ioc.exception.BeanCreationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MiniApplicationContext的抽象类
 */
public abstract class AbstractMiniApplicationContext implements MiniApplicationContext {

    /**
     * 容器解析的所有BeanDefinition
     */
    final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    /**
     * 容器中创建的Bean对象。key是Bean的name
     */
    final Map<String, Object> beans = new HashMap<>();

    /**
     * 根据名称获取Bean
     * @param name
     * @return
     */
    @Override
    public Object getBean(String name){
        return beans.get(name);
        //TODO prototype模式需要动态创建Bean。在有注入的情况下很复杂，需要使用动态代理或CGLIB。
    }

    /**
     * 根据名称和类型获取Bean
     * @param name
     * @param requiredType
     * @return
     * @param <T>
     */
    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        Object bean = getBean(name);
        if (requiredType.isInstance(bean)) {
            return (T) bean;
        }
        return null;
    }

    /**
     * 根据类型获取Bean
     * @param requiredType
     * @return
     * @param <T>
     */
    @Override
    public <T> T getBean(Class<T> requiredType) {
        for (Object bean : beans.values()) {
            if(requiredType.isInstance(bean)){
                return (T)bean;
            }
        }
        return null;
    }

    /**
     * 创建Bean、注入依赖并调用初始化方法
     * @throws BeanCreationException
     */
    void createAndInitBeans() throws BeanCreationException {
        for (BeanDefinition bd : beanDefinitionMap.values()) {
            //如果Bean已经创建，则跳过
            if (beans.get(bd.getBeanName()) != null) {
                continue;
            }
            //递归调用，先创建被依赖的Bean
            for (String dependency : bd.getDependencies()) {
                if (beans.containsKey(dependency)) {
                    continue;
                }
                //TODO 有循环依赖情况
                createBean(beanDefinitionMap.get(dependency));
            }
            createBean(bd);
        }
    }

    /**
     * 根据beanDefinition创建Bean
     * @param beanDefinition
     * @throws BeanCreationException
     */
    private void createBean(BeanDefinition beanDefinition) throws BeanCreationException {
        if (beanDefinition.getFactoryMethodName() != null) {
            createBeanWithFactory(beanDefinition);
        }else{
            createBeanWithConstructor(beanDefinition);
        }
    }

    /**
     * 使用构造函数创建bean
     * @param beanDefinition
     * @throws BeanCreationException
     */
    private void createBeanWithConstructor(BeanDefinition beanDefinition) throws BeanCreationException {
        //异常情况
        Class<?> clazz = beanDefinition.getBeanClass();
        if (clazz == null) {
            throw new BeanCreationException("Bean creation error: the Class of bean '" + beanDefinition.getBeanName() + "' is null");
        }
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            throw new BeanCreationException("Bean creation error: the Class of bean '" + beanDefinition.getBeanName() + "' is not a concrete class");
        }

        //根据参数情况解析构造函数
        Object[] args = new Object[beanDefinition.getConstructorArgs().size()];
        Constructor<?> constructor = resolveConstructor(beanDefinition, args);

        //创建Bean对象，注入属性，调用初始化方法
        try {
            Object object = constructor.newInstance(args);
            beans.put(beanDefinition.getBeanName(),object);
            injectProperties(beanDefinition);
            invokeInitMethods(beanDefinition);
        } catch (Exception e) {
            throw new BeanCreationException("Bean creation error for '"+ beanDefinition.getBeanName()+"':" + e);
        }
    }

    /**
     * 使用工厂创建Bean
     * @param beanDefinition
     * @throws BeanCreationException
     */
    private void createBeanWithFactory(BeanDefinition beanDefinition) throws BeanCreationException {
        String factoryBeanName = beanDefinition.getFactoryBeanName();
        Object factoryBean= (factoryBeanName!=null)? beans.get(factoryBeanName):null;
        Class<?> factoryClass=null;
        if(factoryBean!=null) {
            factoryClass=factoryBean.getClass();
        }else if(beanDefinition.getBeanClass()!=null){
            factoryClass= beanDefinition.getBeanClass();
        }else{
            throw new BeanCreationException(
                    "Bean creation error: both class and factory-bean are missing in bean '"+beanDefinition.getBeanName());
        }

        String factoryMethodName=beanDefinition.getFactoryMethodName();
        try {
            //TODO 有参数的方法
            Method method = factoryClass.getMethod(factoryMethodName);
            Object bean=method.invoke(factoryBean);
            if(bean!=null){
                beans.put(beanDefinition.getBeanName(),bean);
            }
        } catch (Exception e) {
            throw new BeanCreationException(
                    "Bean creation error for bean '"+beanDefinition.getBeanName()+"': "+e.getMessage());
        }
    }


    /**
     * 调用初始化方法
     * @param bd
     * @throws BeanCreationException
     */
    private void invokeInitMethods(BeanDefinition bd) throws BeanCreationException {
        String name = bd.getInitMethodName();
        if(name==null) {return;}
        try {
            Method method = bd.getBeanClass().getMethod("name");
            Object bean=beans.get(bd.getBeanName());
            method.invoke(bean);
        } catch (Exception e) {
            throw new BeanCreationException("invoke init methods of bean '"+bd.getBeanName()+" error:"+e);
        }
    }

    /**
     * 属性注入
     * @param bd BeanDefinition
     * @throws BeanCreationException
     */
    private void injectProperties(BeanDefinition bd) throws BeanCreationException {
        if(bd.getBeanClass()==null) { return;}
        Method[] methods = bd.getBeanClass().getMethods();
        try {
            for (Method method : methods) {
                if (method.getParameterCount() != 1 || !method.getName().startsWith("set")) {
                    continue;
                }
                for (BeanProperty beanProperty : bd.getBeanProperties()) {
                    if (!method.getName().equalsIgnoreCase("set" + beanProperty.getName())) {
                        continue;
                    }
                    Class<?> type = method.getParameters()[0].getType();
                    Object value= null;
                    if (beanProperty.getValue() != null) {
                        value = convertPrimaryValue(beanProperty.getValue(), type);
                    }else if(beanProperty.getRef()!=null){
                       value = beans.get(beanProperty.getRef());
                    }
                    if(value!=null){
                        method.invoke(beans.get(bd.getBeanName()), value);
                    }
                }
            }
        } catch (Exception e) {
            throw new BeanCreationException("Create Bean '" + bd.getBeanName() + "' error. " + e.getMessage());
        }
    }


    /**
     * 由于大部分情况，构造函数参数名在编译后不存在，因此参数名通常不能用于确定参数。
     * 使用index和type，可以无二义地定位构造函数及其参数。但如果这两者不全，Spring需要通过进行尝试和推断。Spring自身推断构造函数的逻辑比较复杂。这里简化一下，只考虑相对确定的情况。
     * @param beanDefinition
     * @param args
     * @return
     * @throws BeanCreationException
     */
    private Constructor<?> resolveConstructor(BeanDefinition beanDefinition, Object[] args) throws BeanCreationException {
        try {
            List<BeanConstructorArg> BeanArgs = beanDefinition.getConstructorArgs();
            if (BeanArgs.isEmpty()) {
                return beanDefinition.getBeanClass().getConstructor();
            }

            if(beanDefinition.getFactoryMethodName()!=null){
                throw new UnsupportedOperationException("Factory-method with parameters is unsupported by now.");
            }
            //解析形参类型paramTypes与实参args
            Class<?>[] paramTypes=new Class[BeanArgs.size()];
            for (BeanConstructorArg beanArg : BeanArgs) {
                Integer index = beanArg.getIndex();
                if(index ==null){
                    throw new UnsupportedOperationException("ConstructorArg without index is unsupported  by now.");
                }
                paramTypes[index] =beanArg.getType();
                if(beanArg.getValue()!=null){
                    args[index]=convertPrimaryValue(beanArg.getValue(),paramTypes[index]);
                }else if(beanArg.getRef()!=null){
                    args[index]= beans.get(beanArg.getRef());
                }
            }

            //匹配构造函数
            for (Constructor<?> constructor : beanDefinition.getBeanClass().getConstructors()) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                if(parameterTypes.length!=BeanArgs.size()) {continue;}
                boolean matched=true;
                for (int i = 0; i < parameterTypes.length; i++) {
                    if(parameterTypes[i].equals(paramTypes[i])) {continue;} //如果参数匹配
                    if(args[i]!=null && parameterTypes[i].isInstance(args[i])) {continue;} //如果ref的Bean的类型匹配
                    matched=false;
                    break;
                }
                if(matched) {
                    return constructor;
                }
             }
            throw new BeanCreationException(
                    "Bean creation error for '"+beanDefinition.getBeanName()+"': no coustructor found in Class" + beanDefinition.getBeanClass().getName());

        } catch (NoSuchMethodException e) {
            throw new BeanCreationException(
                    "Bean creation error for '"+beanDefinition.getBeanName()+"': no coustructor found in Class" + beanDefinition.getBeanClass().getName());
        }

    }

    /**
     * 将value字符串解析为具体值
     * @param valueStr
     * @param type
     * @return
     */
    private static Object convertPrimaryValue(String valueStr, Class<?> type) {
       return  (type == int.class||type == Integer.class) ? Integer.parseInt(valueStr)
                : (type == long.class||type == Long.class) ? Long.parseLong(valueStr)
                : (type == float.class||type == Float.class) ? Float.parseFloat(valueStr)
                : (type == String.class) ? valueStr : null;
    }

}
