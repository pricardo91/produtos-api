package com.mixfiscal.produtos_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tax.api")
@Getter
@Setter
public class TaxIntegrationProperties {
    private String url;
}
