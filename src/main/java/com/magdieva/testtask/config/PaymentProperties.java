package com.magdieva.testtask.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties("payment")
@Data
public class PaymentProperties {
    URI baseUrl;
}
