package edu.whu.demo.entity;
import lombok.Data;

@Data
public class ProductWithSupplier {
    private Product product;
    private Supplier supplier;

    // 构造函数、getter 和 setter 方法
}