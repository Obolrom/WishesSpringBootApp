package io.romix.demo.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.romix.demo.entity.UserEntity;
import io.romix.demo.response.Expense;
import io.romix.demo.util.DbTestHelper;
import io.romix.demo.util.TestDataFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class ExpensesControllerTest {
  @Autowired private MockMvc mockMvc;

  @Autowired private DbTestHelper dbTestHelper;
  @Autowired private TestDataFactory testDataFactory;

  @Test
  void test_createNewExpenseForUser_successfullyCreatedExpense() throws Exception {
    // GIVEN
    DbTestHelper.Transaction transaction = dbTestHelper.startTransactionOnCleanDb();
    UserEntity user = testDataFactory.newUserEntity();
    testDataFactory.newCategoryEntity();
    transaction.commit();

    // WHEN
    MvcResult result = mockMvc
        .perform(
            post("/expenses/{user_id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(
                    Expense.builder()
                        .expenseSum(10.5)
                        .description("Test desc")
                        .timestamp(new Date())
                        .category("Test category")
                        .build())))
        .andExpect(status().isOk())
        .andReturn();

    // THEN
    Expense response = extract(result, Expense.class);
    assertNotNull(response.getId());
    assertEquals("Test category", response.getCategory());
    assertEquals("Test desc", response.getDescription());
  }

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
  public static <T> T extract(MvcResult result, Class<T> type) {
    return getObjectMapper().readValue(result.getResponse().getContentAsString(), type);
  }
}