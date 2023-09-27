package org.example;
import edu.whu.MyClass;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

// 按两次 Shift 打开“随处搜索”对话框并输入 `show whitespaces`，
// 然后按 Enter 键。现在，您可以在代码中看到空格字符。


        public class Main {
            public static void main(String[] args) throws Exception
            {

                Properties properties = new Properties();


                InputStream in = new FileInputStream("src/main/resources/myapp.properties");

                properties.load(in);
                String  a=properties.getProperty("bootstrapClass");
                MyClass zuoye=new MyClass();
                System.out.println(a);
                in.close();
                zuoye.init();





            }
        }

