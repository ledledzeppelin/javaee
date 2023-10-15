package edu.whu.ioc.config;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;

import java.util.*;

@Data
@NoArgsConstructor
public class BeanDefinition {
    /**
     * Bean的Scope类型
     */
    public enum Scope {SINGLETON,PROTOTYPE};

    /**
     * Bean的Autowire模式
     */
    public enum Autowire {NONE,BYTYPE,BYNAME};

    /**
     * Bean的Name
     */
    private String beanName;

    /**
     * Bean的类
     */
    private Class<?> beanClass;

    /**
     * Bean的Scope类型
     */
    private Scope scope = Scope.SINGLETON;

    /**
     * Bean的Autowire模式
     */
    private Autowire autowireMode = Autowire.NONE;
    /**
     * Bean的factoryBean
     */
    private String factoryBeanName;

    /**
     * Bean的factory-method
     */
    private String factoryMethodName;

    /**
     * Bean的属性定义
     */
    private final List<BeanProperty> beanProperties = new ArrayList<>();
    /**
     * Bean的构造函数参数表
     */
    private final List<BeanConstructorArg> constructorArgs = new ArrayList<>();

    /**
     * Bean的init-method
     */
    private String initMethodName;

    /**
     * Bean的destroy-method
     */
    private String destroyMethodName;

    /**
     * 获取当前Bean依赖的bean
     * @return
     */
    public Set<String> getDependencies(){
        Set<String> dependencies=new HashSet<>();
        constructorArgs.forEach(c-> {
            if(c.getRef()!=null){
                dependencies.add(c.getRef());
            }
        });
        if(factoryBeanName!=null){
            dependencies.add(factoryBeanName);
        }
        return dependencies;
    }
}
