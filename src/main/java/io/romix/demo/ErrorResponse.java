package io.romix.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
  private Integer code = null;
  private String message = null;

  public ErrorResponse(HttpStatus status, String message) {
    this.setCode(status.value());
    this.setMessage(message);
  }

  public static ResponseEntity<ErrorResponse> getResponseEntity(HttpStatus status, String message) {
    return new ResponseEntity<>(new ErrorResponse(status, message), status);
  }
}
