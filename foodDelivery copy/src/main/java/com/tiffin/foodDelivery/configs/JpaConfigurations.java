package com.tiffin.foodDelivery.configs;

import com.tiffin.foodDelivery.audits.ApplicationAuditAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing(auditorAwareRef = "applicationAuditAware")
public class JpaConfigurations {

    @Bean
    public ApplicationAuditAware applicationAuditAware() {
        return new ApplicationAuditAware();
    }
}

