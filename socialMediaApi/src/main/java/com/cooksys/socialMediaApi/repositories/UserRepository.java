package com.cooksys.socialMediaApi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cooksys.socialMediaApi.entities.Tweet;
import com.cooksys.socialMediaApi.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByDeletedFalse();

	User findByCredentialsIgnoreCaseUsernameAndDeletedFalse(String username);

	boolean existsByCredentialsUsername(String username);

	@Query("SELECT t FROM Tweet t WHERE t.deleted = false AND LOWER(t.content) LIKE LOWER(CONCAT('%', :mention, '%')) ORDER BY t.posted DESC")
	List<Tweet> findByMentionedUsernameDeletedFalse(@Param("mention") String mention);

}