package io.romix.demo.repository;

import io.romix.demo.entity.WishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<WishEntity, Long> {
}
