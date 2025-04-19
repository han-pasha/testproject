package com.problem.test.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.Instant;

@Provider
public class GlobalExceptionThrowableMapper implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    exception.printStackTrace();

    ErrorResponse error = new ErrorResponse(
        "Internal server error",
        exception.getClass().getSimpleName(),
        Instant.now().toString()
    );

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
               .entity(error)
               .type(MediaType.APPLICATION_JSON)
               .build();
  }

  public static class ErrorResponse {
    public String message;
    public String error;
    public String timestamp;

    public ErrorResponse(String message, String error, String timestamp) {
      this.message = message;
      this.error = error;
      this.timestamp = timestamp;
    }
  }
}
