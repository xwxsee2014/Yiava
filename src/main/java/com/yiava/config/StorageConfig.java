package com.yiava.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration for document storage
 */
@Configuration
@PropertySource("classpath:application.yml")
public class StorageConfig {

    @Value("${yiava.storage.base-path:storage/documents}")
    private String basePath;

    @Value("${yiava.storage.retention-days:90}")
    private int retentionDays;

    public String getBasePath() {
        return basePath;
    }

    public int getRetentionDays() {
        return retentionDays;
    }
}
