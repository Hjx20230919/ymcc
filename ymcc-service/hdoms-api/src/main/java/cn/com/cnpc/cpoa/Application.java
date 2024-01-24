package cn.com.cnpc.cpoa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
//import tk.mybatis.spring.annotation.MapperScan;
//import tk.mybatis.spring.annotation.MapperScan;

/**
 * 应用入口
 *
 * @author scchenyong@189.cn
 * @create 2018-12-24
 */
@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"cn.com.cnpc.cpoa"})
@MapperScan("cn.com.cnpc.cpoa.mapper")
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@EnableSwagger2
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
