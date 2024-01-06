package io.romix.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class CustomExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handlePersonIn4Exception(CustomException e) {
    var httpStatus = e.getStatus();
    var message = e.getMessage();
    log.info(httpStatus.getReasonPhrase(), message);
    return ErrorResponse.getResponseEntity(httpStatus, message);
  }
}
