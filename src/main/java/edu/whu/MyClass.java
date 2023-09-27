package edu.whu;

import edu.whu.framework.InitMethod;
public class MyClass {
String classroom;
int number;
    @InitMethod
    public void init()
    {
classroom="计算机学院b303";
number=50;
System.out.println(classroom+number);
    }

   /* public @interface initMethod {
        String value() default "";
    }*/
}