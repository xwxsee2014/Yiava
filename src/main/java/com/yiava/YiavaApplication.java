package com.yiava;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class
 *
 * This is the entry point for the Yiava CRUD application.
 * The @SpringBootApplication annotation enables:
 * - @Configuration: Marks this as a source of bean definitions
 * - @EnableAutoConfiguration: Enables Spring Boot's auto-configuration mechanism
 * - @ComponentScan: Enables component scanning in the com.yiava package and sub-packages
 *
 * Additional annotations:
 * - @MapperScan: Scans for MyBatis mapper interfaces in com.yiava.mapper package
 */
@SpringBootApplication
@MapperScan("com.yiava.mapper")
public class YiavaApplication {

    /**
     * Main method - Spring Boot entry point
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(YiavaApplication.class, args);
        System.out.println("""

                ╔══════════════════════════════════════════════════════════════╗
                ║                   Yiava Application                          ║
                ║              Spring Boot CRUD API Started                    ║
                ║                                                              ║
                ║  • Server running on: http://localhost:8080/api              ║
                ║  • Actuator endpoints: http://localhost:8080/actuator        ║
                ║  • API Documentation: http://localhost:8080/swagger-ui.html  ║
                ║  • Druid Console: http://localhost:8080/druid/               ║
                ╚══════════════════════════════════════════════════════════════╝
                """);
    }
}
