package com.manzar.telegramweatherbot.config;

import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class. Contains application bean definitions.
 */
@Configuration
@EnableScheduling
public class AppConfig {

  @Value("${default.timezone}")
  private String defaultTimeZone;

  @Value("${api.openweathermap.key}")
  private String weatherMapApiKey;

  @PostConstruct
  void started() {
    TimeZone defaultZone = TimeZone.getTimeZone(defaultTimeZone);
    TimeZone.setDefault(defaultZone);
  }

  @Bean
  public OpenWeatherMapClient openWeatherMapClient() {
    return new OpenWeatherMapClient(weatherMapApiKey);
  }

  /**
   * Creates RestTemplate bean without error handler.
   */
  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplateBuilder()
        .errorHandler(new DefaultResponseErrorHandler() {
          @Override
          public void handleError(@NonNull ClientHttpResponse response) {
          }
        })
        .build();
  }

  /**
   * Creates MessageSource bean to read messages from resource bundle with UTF-8 encoding.
   */
  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("message");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}