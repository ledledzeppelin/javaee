package edu.whu.ioc.parser;

import edu.whu.ioc.config.BeanConstructorArg;
import edu.whu.ioc.config.BeanDefinition;
import edu.whu.ioc.config.BeanProperty;
import edu.whu.ioc.exception.BeanParseException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * BeanDefinition的校验器
 */
public class BeanDefValidator {


    public static void validate(Map<String, BeanDefinition> beanDefinitions) throws BeanParseException {
        for (BeanDefinition beanDef : beanDefinitions.values()) {
            checkBeanDef(beanDef);
        }
        checkDependency(beanDefinitions);

    }

    private static void checkBeanDef(BeanDefinition beanDef) throws BeanParseException {
        if(beanDef.getFactoryBeanName()==null && beanDef.getBeanClass()==null){
            throw new BeanParseException("the Class of bean '"+beanDef.getBeanName()+" is missing");
        }
        if(beanDef.getFactoryBeanName()!=null && beanDef.getFactoryMethodName()==null){
            throw new BeanParseException("the factory-method of bean '"+beanDef.getBeanName()+" is missing");
        }
        checkPropertyDef(beanDef);
        checkConstructorArgs(beanDef);

    }

    private static void checkConstructorArgs(BeanDefinition beanDef) throws BeanParseException {
        //这里严格限定。在Spring中更宽松
        Set<Integer> indexes=new HashSet<>();
        for (BeanConstructorArg constructorArg : beanDef.getConstructorArgs()) {
            indexes.add(constructorArg.getIndex());
            if (constructorArg.getValue() != null && constructorArg.getType() == null) {
                throw new BeanParseException("the constructorArg in bean'"+
                        beanDef.getBeanName()+" is not defined correctly: the argument of primary type must have a specific type");
            }
        }
        if(indexes.size()!=beanDef.getConstructorArgs().size()){
            throw new BeanParseException("the indexes of constructorArg of bean'"+
                    beanDef.getBeanName()+" is not defiened correctly");
        }
    }

    private static void checkPropertyDef(BeanDefinition beanDef) throws BeanParseException {
        for (BeanProperty beanProperty : beanDef.getBeanProperties()) {
            if(beanProperty.getName()==null){
                throw new BeanParseException("the property of bean'"+beanDef.getBeanName()+" has no name");
            }
            if(beanProperty.getValue()==null && beanProperty.getRef()==null){
                throw new BeanParseException("the property '"+beanProperty.getName()+"' of bean'"
                        +beanDef.getBeanName()+" has neither value nor ref");
            }
        }
    }

    private static void checkDependency(Map<String, BeanDefinition> beanDefinitions) throws BeanParseException {
        for (BeanDefinition beanDef : beanDefinitions.values()) {
            for (String dependency : beanDef.getDependencies()) {
                if(!beanDefinitions.containsKey(dependency)){
                    throw new BeanParseException("the ref '"+dependency+"' of Bean '"+
                            beanDef.getBeanName()+" is undefined");
                }
            }
        }
    }
}
