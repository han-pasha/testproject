package com.problem.test.configuration.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfiguration {

  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule validationModule = new SimpleModule();
    validationModule.setDeserializerModifier(new BeanDeserializerModifierWithValidation());
    mapper.registerModule(validationModule);
    return mapper;
  }

  @Bean(name = "objectMapperWithoutValidation")
  public ObjectMapper normalObjectMapper() {
    return new ObjectMapper();
  }
}
