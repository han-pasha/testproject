package com.problem.test.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.MediaType;

@Provider
public class GlobalRuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
  private static final Logger log = LoggerFactory.getLogger(GlobalRuntimeExceptionMapper.class);

  @Override
  public Response toResponse(RuntimeException exception) {
    log.error("Unhandled exception occured", exception);
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
               .entity(new ErrorResponse(exception.getMessage().isEmpty() ? "An unexpected error occurred. Please try again later." : exception.getMessage()))
               .type(MediaType.APPLICATION_JSON)
               .build();
  }

  public static class ErrorResponse {
    private String error;

    public ErrorResponse(String error) {
      this.error = error;
    }

    public String getError() {
      return error;
    }

    public void setError(String error) {
      this.error = error;
    }
  }
}
