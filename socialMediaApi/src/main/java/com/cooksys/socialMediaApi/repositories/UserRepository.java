package com.cooksys.socialMediaApi.repositories;

import com.cooksys.socialMediaApi.entities.User;
import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCredentialsUsername(String username);

    Optional<User> findByCredentialsIgnoreCaseUsernameAndDeletedFalse(String username);

    List<User> findByDeletedFalse();

    boolean existsByCredentialsUsername(String username);

}
