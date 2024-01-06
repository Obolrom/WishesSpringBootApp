package io.romix.demo.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

public class TestUtils {

  private static final ObjectMapper objectMapper = getObjectMapper();

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    return objectMapper;
  }

  @SneakyThrows
  public static String asJsonString(final Object obj) {
    return getObjectMapper().writeValueAsString(obj);
  }

  @SneakyThrows
  public static String extractMessage(MvcResult result) {
    return (String)
        objectMapper.readValue(result.getResponse().getContentAsString(), Map.class).get("message");
  }

  @SneakyThrows
  public static <T> T extract(MvcResult result, Class<T> type) {
    return getObjectMapper().readValue(result.getResponse().getContentAsString(), type);
  }
}
