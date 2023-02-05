package com.manzar.telegramweatherbot.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class. Contains application bean definitions.
 */
@Configuration
public class AppConfig {


  /**
   * Creates RestTemplate bean without error handler.
   */
  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplateBuilder()
        .errorHandler(new DefaultResponseErrorHandler() {
          @Override
          public void handleError(ClientHttpResponse response) {
          }
        })
        .build();
  }
}
