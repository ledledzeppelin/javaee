package edu.whu.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.whu.demo.entity.Product;
import edu.whu.demo.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 使用MockMVC模拟HTTP请求，调用Controller代码。
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 将ProductService注入到测试用例里面，目的是为了验证调用后结果是否正确。
     */
    @Autowired
    ProductService productService;


    @AfterEach
    private void clean(){
        productService.deleteAll();
    }

    @BeforeEach
    private void initData(){
        Product product=new Product();
        product.setId(1);
        product.setName("IPhone 13");
        product.setPrice(8000);
        product.setProductType("SND-dff");
        product.setCategory("手机");
        product.setStockQuantity(20);
        productService.addProduct(product);
    }


    @Test
    public void testAddProduct() throws Exception {
        Product product=new Product();
        product.setId(2);
        product.setName("IPhone 18");
        product.setPrice(12000);
        product.setProductType("SND-dff");
        product.setCategory("手机");
        product.setStockQuantity(10);

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/products")
                .content(new ObjectMapper().writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2));

        //验证是否添加成功
        assertNotNull(productService.getProduct(2));

    }


    @Test
    public void testFindProduct() throws Exception {
        Product product=new Product();
        product.setId(2);
        product.setName("IPhone 18");
        product.setPrice(12000);
        product.setProductType("SND-dff");
        product.setCategory("手机");
        product.setStockQuantity(10);
        productService.addProduct(product);

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .get("/products/2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(2));

        resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .get("/products?name=IPhone&quantity=15")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(2));
    }


    @Test
    public void testUpdateProduct() throws Exception {
        Product product=  productService.getProduct(1);
        product.setStockQuantity(15);

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .put("/products/1")
                .content(new ObjectMapper().writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //resultActions.andDo(print());
        resultActions.andExpect(status().isOk());

        //验证是否修改成功
        product=  productService.getProduct(1);
        assertEquals(15,product.getStockQuantity());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        assertNull(productService.getProduct(1));

    }


}
