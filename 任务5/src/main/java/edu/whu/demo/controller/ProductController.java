package edu.whu.demo.controller;

import edu.whu.demo.entity.Product;
import edu.whu.demo.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "商品管理")      //这是Swagger注解，用于API文档的生成和描述。tags属性用于为API添加标签
@RestController            //这是Spring框架的注解，用于标识一个类是Spring MVC控制器中的RESTful控制器。它告诉Spring框架，该类的方法返回的内容将直接以JSON或XML等格式发送到客户端，而不是渲染为HTML视图。
@RequestMapping("products")      //在这里，它指定了控制器处理的请求路径，即"/products"。当客户端发送请求到"/products"时，与该请求相关的处理方法将被调用。
public class ProductController {
    @Autowired    //它用于自动装配（注入）依赖对象，通常用于将其他 Spring 管理的 bean 注入到当前类中。这个注解可以用在字段、构造函数、Setter 方法等地方，以实现依赖注入
    ProductService productService;

    @ApiOperation("根据Id查询商品")          //用于对 API 接口进行文档化描述。在该注解中，通常会提供对接口操作的描述，例如接口的目的、用途、输入参数、输出结果等。这样的描述可以生成 API 文档，帮助其他开发人员理解如何使用该接口
    @GetMapping("/{id}")           //用于将 HTTP GET 请求映射到指定的控制器方法。在上下文中，它表示处理 GET 请求的方法，并指定了请求的路径。在 RESTful API 中，通常使用它来定义获取资源的操作。
    public ResponseEntity<Product> getProduct(@ApiParam("商品Id")@PathVariable long id){
        Product result = productService.getProduct(id);
        if(result==null) {
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(result);
        }
    }

    @ApiOperation("根据条件查询商品")
    @GetMapping("")
    public ResponseEntity<List<Product>> findProduct(@ApiParam("商品名称")String name, @ApiParam("商品数量")Float quantity){
        List<Product> result = productService.findProduct(name,quantity);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("添加商品")
    @PostMapping("")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Product result = productService.addProduct(product);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("修改商品信息")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable long id,@RequestBody Product product){
        productService.updateProduct(id,product);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("删除商品")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

}
