package edu.whu.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author jiaxy
 */

@Data         //生成对应的 getter 和 setter 方法，toString() 方法，equals() 和 hashCode() 方法，以及带有所有参数的构造方法。
@NoArgsConstructor     //Lombok 提供的注解，它用于自动生成一个无参数的构造方法。
@EqualsAndHashCode(callSuper = false)
public class Product {

    //产品Id
    @TableField(value = "id")
    long id;
    //产品名称
    String name;
    //产品单价
    float price;
    //库存数量
    float stockQuantity;
    //产品类别
    String category;
    //产品类型
    String productType;
    //产品描述
    String description;


}
