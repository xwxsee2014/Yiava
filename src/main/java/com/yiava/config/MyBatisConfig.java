package com.yiava.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis configuration class
 * Scans for mapper interfaces in the com.yiava.mapper package
 * Enables automatic discovery and registration of MyBatis mappers
 */
@Configuration
@MapperScan("com.yiava.mapper")
public class MyBatisConfig {

    /**
     * MyBatis is auto-configured by Spring Boot
     * This class serves as a marker for the mapper scanning configuration
     *
     * The following properties are configured in application.yml:
     * - mybatis.mapper-locations: classpath:mapper/*.xml
     * - mybatis.type-aliases-package: com.yiava.entity
     * - mybatis.configuration.map-underscore-to-camel-case: true
     */
}
