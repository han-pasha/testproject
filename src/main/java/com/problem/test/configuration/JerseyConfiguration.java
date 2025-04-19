package com.problem.test.configuration;

import com.problem.test.exception.GlobalExceptionThrowableMapper;
import com.problem.test.exception.GlobalRuntimeExceptionMapper;
import com.problem.test.rest.ProductResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfiguration extends ResourceConfig {
  public JerseyConfiguration() {
    register(ProductResource.class);
    register(GlobalRuntimeExceptionMapper.class);
    register(GlobalExceptionThrowableMapper.class);
    register(ProductResource.class);
    property("jersey.config.server.tracing.type", "ALL");
    packages("org.glassfish.jersey.examples.beanvalidation");
  }
}
