package com.cooksys.socialMediaApi.repositories;

import java.util.List;
<<<<<<< HEAD
import java.util.Optional;
=======
>>>>>>> 927c519a88d3223a9d8d3222e1cea821dd130bcd

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cooksys.socialMediaApi.entities.Tweet;
import com.cooksys.socialMediaApi.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

<<<<<<< HEAD
	List<User> findByDeletedFalse();
=======
    Optional<User> findByCredentialsUsername(String username);
>>>>>>> 927c519a88d3223a9d8d3222e1cea821dd130bcd

	Optional<User> findByCredentialsIgnoreCaseUsernameAndDeletedFalse(String username);

<<<<<<< HEAD
	boolean existsByCredentialsUsername(String username);
=======
    List<User> findByDeletedFalse();

    boolean existsByCredentialsUsername(String username);
>>>>>>> 927c519a88d3223a9d8d3222e1cea821dd130bcd

	@Query("SELECT t FROM Tweet t JOIN t.mentionedUsers u WHERE t.deleted = false AND u.credentials.username = :username ORDER BY t.posted DESC")
	List<Tweet> findByMentionedUsernameDeletedFalse(@Param("username") String username);

}