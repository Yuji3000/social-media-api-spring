package com.cooksys.socialMediaApi.repositories;

import com.cooksys.socialMediaApi.entities.User;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByDeletedFalse();

    Optional<User> findByCredentialsIgnoreCaseUsernameAndDeletedFalse(String username);

    boolean existsByCredentialsUsername(String username);

}
