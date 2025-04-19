package com.problem.test.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * This class is solely created for the sake of integration testing.
 * Although rest template is deprecated, its easier to implement than web client.
 */
@Configuration
public class RestTemplateConfig {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
