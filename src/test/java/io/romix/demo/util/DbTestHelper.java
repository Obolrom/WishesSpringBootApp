package io.romix.demo.util;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.sql.Statement;

@Component
@RequiredArgsConstructor
public class DbTestHelper {
  private final EntityManager entityManager;
  private final PlatformTransactionManager platformTransactionManager;

  public interface Transaction {
    void commit();
  }

  Transaction startTransaction() {
    TransactionStatus transactionStatus =
        platformTransactionManager.getTransaction(TransactionDefinition.withDefaults());
    return () -> platformTransactionManager.commit(transactionStatus);
  }

  public Transaction startTransactionOnCleanDb() {
    Transaction transaction = startTransaction();
    cleanupDb();
    return transaction;
  }

  void cleanupDb() {
    try (Session session = entityManager.unwrap(Session.class)) {
      session.doWork(
          connection -> {
            Statement statement = connection.createStatement();
            for (String table : tables) {
              statement.addBatch("delete from " + table);
            }

            statement.executeBatch();
          });
    }
  }

  private static final String[] tables =
      new String[] {
          "expenses",
          "categories",
          "users",
      };
}
