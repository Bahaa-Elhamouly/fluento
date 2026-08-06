package com.fluento.features.user.repository;

import com.fluento.features.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmailIgnoreCaseAndDeletedAtIsNull(
            String email
    );

    boolean existsByEmailIgnoreCaseAndDeletedAtIsNull(
            String email
    );

    List<User> findByDeletedAtIsNullOrderByCreatedAtDesc();

    Optional<User> findByIdAndDeletedAtIsNull(UUID id);

    long countByDeletedAtIsNull();

    long countByEnabledTrueAndDeletedAtIsNull();
}
