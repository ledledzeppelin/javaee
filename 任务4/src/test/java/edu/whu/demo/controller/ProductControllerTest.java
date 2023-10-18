package edu.whu.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.whu.demo.entity.Product;
import edu.whu.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest

public class ProductControllerTest {
    @Autowired
    ProductService productService;
    @Autowired
    Product product;

    private MockMvc mockMvc;
    private WebApplicationContext webApplicationContext;


    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddProduct() throws Exception {
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(10);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk());
    }

    // 添加更多的测试用例
}