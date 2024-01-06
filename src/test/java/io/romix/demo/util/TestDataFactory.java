package io.romix.demo.util;

import io.romix.demo.entity.CategoryEntity;
import io.romix.demo.entity.ExpenseEntity;
import io.romix.demo.entity.Role;
import io.romix.demo.entity.UserEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataFactory {
  private final EntityManager entityManager;
  private static final EntityCustomizer nop = o -> {};

  public interface EntityCustomizer<Entity> {
    void customize(Entity entity);
  }

  private <Entity> Entity customizeAndPersist(
      EntityCustomizer<Entity> entityCustomizer, Entity entity) {
    entityCustomizer.customize(entity);
    entityManager.persist(entity);
    return entity;
  }

  public UserEntity newUserEntity() {
    return newUserEntity(nop);
  }

  public UserEntity newUserEntity(EntityCustomizer<UserEntity> userCustomizer) {
    return customizeAndPersist(
        userCustomizer,
        new UserEntity(
            null,
            "test_username",
            "password",
            Role.USER,
            "http://photo.jpg",
            "test description",
            new Date(),
            List.of()
        ));
  }

  public ExpenseEntity newExpenseEntity(CategoryEntity category, UserEntity user) {
    return newExpenseEntity(category, user, nop);
  }

  public ExpenseEntity newExpenseEntity(
      CategoryEntity category,
      UserEntity user,
      EntityCustomizer<ExpenseEntity> expenseCustomizer) {
    return customizeAndPersist(
        expenseCustomizer,
        new ExpenseEntity(
            null,
            10.7,
            category,
            new Date(),
            "test description",
            user));
  }

  public CategoryEntity newCategoryEntity() {
    return newCategoryEntity(nop);
  }

  public CategoryEntity newCategoryEntity(EntityCustomizer<CategoryEntity> categoryCustomizer) {
    return customizeAndPersist(
        categoryCustomizer,
        new CategoryEntity(
            null,
            "Test category"
        ));
  }
}
