package com.cooksys.socialMediaApi.repositories;

import com.cooksys.socialMediaApi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
