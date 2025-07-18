package com.project.admin;

import org.apache.shiro.env.Environment;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

//SET GLOBAL sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
/**
 * @author jas
 * @date 2025/6/26
 */
@SpringBootApplication(
        exclude = {DataSourceAutoConfiguration.class},
        scanBasePackages = {
                "com.project.admin",
                "com.project.framework",  // 扫描framework模块下的所有组件
                "com.project.common"      // 其他可能需要扫描的模块
        }
)
@ComponentScan("com.project")
@MapperScan("com.**.mapper")
public class AdminApplication {

    public static void main(String[] args) {
       SpringApplication.run(AdminApplication.class, args);


    }

}
