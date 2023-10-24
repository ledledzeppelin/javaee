package edu.whu.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.whu.demo.entity.Product;
import edu.whu.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 使用MockMVC模拟HTTP请求，使用MockBean隔离被测试代码所依赖的组件。
 * 在实际的软件项目中，一般使用MockBean将一些依赖外部环境（数据库、公共API等）隔离开，从而让测试可以更方便的启动。
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ProjuctControllerMockTest {

    @Autowired
    private MockMvc mockMvc;
    /**
     * 使用MockBean来替换真实的ProductService，目的是只关注与ProjuctController部分代码的测试，而不测试ProductService的代码。
     */
    @MockBean
    private ProductService productService;

    @Test
    void getProduct_ValidId_ReturnsProduct() throws Exception {
        // 准备测试数据
        long productId = 1;
        Product expectedProduct = new Product();
        expectedProduct.setId(productId);
        when(productService.getProduct(productId)).thenReturn(expectedProduct);

        // 执行请求并验证结果
        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(productId));

    }

    @Test
    void getProduct_InvalidId_ReturnsNoContent() throws Exception {
        // 准备测试数据
        long productId = 1;
        when(productService.getProduct(productId)).thenReturn(null);

        // 执行请求并验证结果
        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void findProduct_ReturnsListOfProducts() throws Exception {
        // 准备测试数据
        String productName = "Test Product";
        Float productQuantity = 10.0f;
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(new Product());
        when(productService.findProduct(productName, productQuantity)).thenReturn(expectedProducts);

        // 执行请求并验证结果
        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .param("name", productName)
                        .param("quantity", productQuantity.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists());
    }

    @Test
    void addProduct_ValidProduct_ReturnsProduct() throws Exception {
        // 准备测试数据
        Product inputProduct = new Product();
        inputProduct.setName("Test Product");
        inputProduct.setPrice(10);

        // 执行请求并验证结果
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(inputProduct)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // 验证productService的调用参数是否正确
        verify(productService).addProduct(inputProduct);
    }

}