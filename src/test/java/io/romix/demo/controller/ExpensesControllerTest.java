package io.romix.demo.controller;

import io.romix.demo.entity.CategoryEntity;
import io.romix.demo.entity.ExpenseEntity;
import io.romix.demo.entity.UserEntity;
import io.romix.demo.expenses.ExpenseCreateRequest;
import io.romix.demo.repository.ExpenseRepository;
import io.romix.demo.response.Expense;
import io.romix.demo.util.DbTestHelper;
import io.romix.demo.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static io.romix.demo.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class ExpensesControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ExpenseRepository expenseRepository;

  @Autowired private DbTestHelper dbTestHelper;
  @Autowired private TestDataFactory testDataFactory;

  @Test
  void test_createNewExpenseForUser_successfullyCreatedExpense() throws Exception {
    // GIVEN
    DbTestHelper.Transaction transaction = dbTestHelper.startTransactionOnCleanDb();
    UserEntity user = testDataFactory.newUserEntity();
    CategoryEntity category = testDataFactory.newCategoryEntity();
    transaction.commit();

    // WHEN
    MvcResult result = mockMvc
        .perform(
            post("/expenses/{user_id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(
                    ExpenseCreateRequest.builder()
                        .expenseSum(10.5)
                        .description("Test desc")
                        .categoryId(category.getId())
                        .build())))
        .andExpect(status().isOk())
        .andReturn();

    // THEN
    Expense response = extract(result, Expense.class);
    assertNotNull(response.getId());
    assertEquals("Test category", response.getCategory());
    assertEquals("Test desc", response.getDescription());
  }

  @Test
  void test_createNewExpenseForUser_categoryNotFound() throws Exception {
    // GIVEN
    DbTestHelper.Transaction transaction = dbTestHelper.startTransactionOnCleanDb();
    UserEntity user = testDataFactory.newUserEntity();
    CategoryEntity category = testDataFactory.newCategoryEntity();
    transaction.commit();

    // WHEN
    Long invalidCategoryId = category.getId() + 1;
    MvcResult result = mockMvc
        .perform(
            post("/expenses/{user_id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(
                    ExpenseCreateRequest.builder()
                        .expenseSum(10.5)
                        .description("Test desc")
                        .categoryId(invalidCategoryId)
                        .build())))
        .andExpect(status().isNotFound())
        .andReturn();

    // THEN
    assertEquals(
        String.format("No category was found with id %d", invalidCategoryId),
        extractMessage(result));
  }

  @Test
  void test_deleteExpense_successfullyDeleted() throws Exception {
    // GIVEN
    DbTestHelper.Transaction transaction = dbTestHelper.startTransactionOnCleanDb();
    UserEntity user = testDataFactory.newUserEntity();
    CategoryEntity category = testDataFactory.newCategoryEntity();
    ExpenseEntity expense = testDataFactory.newExpenseEntity(category, user);
    transaction.commit();

    // WHEN
    mockMvc
        .perform(
            delete("/expenses/{expenseId}", expense.getId()))
        .andExpect(status().isOk())
        .andReturn();

    // THEN
    assertTrue(expenseRepository.findAll().isEmpty());
  }

  @Test
  void test_deleteExpense_notFoundById() throws Exception {
    // GIVEN
    DbTestHelper.Transaction transaction = dbTestHelper.startTransactionOnCleanDb();
    UserEntity user = testDataFactory.newUserEntity();
    CategoryEntity category = testDataFactory.newCategoryEntity();
    ExpenseEntity expense = testDataFactory.newExpenseEntity(category, user);
    transaction.commit();

    // WHEN
    Long invalidExpenseId = expense.getId() + 1;
    MvcResult result = mockMvc
        .perform(
            delete("/expenses/{expenseId}", invalidExpenseId))
        .andExpect(status().isNotFound())
        .andReturn();

    // THEN
    assertEquals(
        String.format("No expense was found with id %d", invalidExpenseId),
        extractMessage(result));
  }
}