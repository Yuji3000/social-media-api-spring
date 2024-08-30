package com.cooksys.socialMediaApi.repositories;

import java.util.List;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cooksys.socialMediaApi.entities.Tweet;
import com.cooksys.socialMediaApi.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByDeletedFalse();

    Optional<User> findByCredentialsIgnoreCaseUsername(String username);

	Optional<User> findByCredentialsIgnoreCaseUsernameAndDeletedFalse(String username);

	boolean existsByCredentialsIgnoreCaseUsername(String username);

	boolean existsByCredentialsIgnoreCaseUsernameAndDeletedIsFalse(String username);

	@Query("SELECT t FROM Tweet t JOIN t.mentionedUsers u WHERE t.deleted = false AND u.credentials.username = :username ORDER BY t.posted DESC")
	List<Tweet> findByMentionedUsernameDeletedFalse(@Param("username") String username);
	
	User findByCredentialsUsernameAndCredentialsPasswordAndDeletedFalse(String username, String password);

}