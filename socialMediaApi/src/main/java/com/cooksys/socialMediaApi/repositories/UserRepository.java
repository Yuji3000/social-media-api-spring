package com.cooksys.socialMediaApi.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.socialMediaApi.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByDeletedFalse();

    Optional<User> findByCredentialsIgnoreCaseUsernameAndDeletedFalse(String username);

    boolean existsByCredentialsUsername(String username);

}
