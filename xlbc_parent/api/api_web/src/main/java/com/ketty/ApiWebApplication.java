package com.ketty;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;
/**
 * EnableMBeanExport 解决Jmx重复注册bean的问题
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@MapperScan(basePackages={"com.ketty.api_mapper"})
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@EnableCaching
public class ApiWebApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ApiWebApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiWebApplication.class, args);
    }

}
