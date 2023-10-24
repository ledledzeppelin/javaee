package edu.whu.demo.controller;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@ComponentScan("edu.whu.demo")
public class ProductServiceText {

    public static void main(String[] args) {
        // 创建代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/your_database");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("hf20030819");
        autoGenerator.setDataSource(dataSourceConfig);

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir") + "/sql"); // 指定 SQL 文件的输出目录
        globalConfig.setAuthor("Your Name");
        globalConfig.setOpen(false);
        autoGenerator.setGlobalConfig(globalConfig);

        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("your.package");
        autoGenerator.setPackageInfo(packageConfig);

        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setInclude("product", "supplier"); // 要生成的表名
        autoGenerator.setStrategy(strategyConfig);

        // 执行生成
        autoGenerator.execute();
       @Autowired
        private ProductService productService;

        @Test
        public void testInsertProduct() {
            Product product = new Product();
            product.setName("Product 1");
            product.setDescription("Description 1");
            productService.insert(product);

            // Add assertions to verify the insertion
        }

        @Test
        public void testSelectProduct() {
            Long productId = 1L;
            Product product = productService.selectById(productId);

            // Add assertions to verify the retrieved product
        }
    }
}


