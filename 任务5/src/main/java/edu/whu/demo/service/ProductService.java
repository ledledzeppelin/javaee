package edu.whu.demo.service;


import edu.whu.demo.entity.Product;
import org.springframework.stereotype.Service;

import java.util.*;


@Service       //它用于标识一个类是服务（Service）类，通常用于表示业务逻辑层的组件。在 Spring 应用中，@Service 注解用于告诉 Spring 容器将被注解的类识别为一个服务，可以自动进行组件扫描和依赖注入。
public class ProductService {

    // 创建线程安全的Map


    private Map<Long, Product> productMap =
            Collections.synchronizedMap(new HashMap<Long, Product>());  //这段代码创建了一个 Map 数据结构，用于存储产品信息，其中键是 Long 类型的产品 ID，值是 Product 类型的产品对象。

    /**
     * 添加商品
     * @param product
     * @return
     */
    public Product addProduct(Product product) {
        productMap.put(product.getId(), product);
        return product;
    }

    /**
     * 根据Id查找
     * @param id
     * @return
     */
    public Product getProduct(long id) {
        return productMap.get(id);
    }

    /**
     * 根据名称和库存查找
     * @param name
     * @param quantity
     * @return
     */
    public List<Product> findProduct(String name, Float quantity) {
        List<Product> result=new ArrayList<>();
        for (Product product: productMap.values()){
            if (name!=null && !product.getName().contains(name)) {
                continue;
            }
            if (quantity!=null && product.getStockQuantity()>= quantity) {
                continue;
            }
            result.add(product);
        }
        return result;
    }

    /**
     * 更新商品信息
     * @param id
     * @param product
     */
    public void updateProduct(long id, Product product) {
        Product product1  = productMap.get(id);
        if(product1!=null){
            productMap.put(id,product);
        }
    }

    /**
     * 删除商品
     * @param id
     */
    public void deleteProduct(long id) {
        productMap.remove(id);
    }

    /**
     * 删除所有商品
     */
    public void deleteAll(){
        productMap.clear();
    }


}
