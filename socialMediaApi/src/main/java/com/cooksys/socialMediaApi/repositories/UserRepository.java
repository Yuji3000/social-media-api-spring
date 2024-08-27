package com.cooksys.socialMediaApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TODO: Add User import
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
