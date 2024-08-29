package com.cooksys.socialMediaApi.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.socialMediaApi.entities.Tweet;


@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
	
	List<Tweet> findByDeletedFalseOrderByPostedDesc();
	
	Optional<Tweet> findByIdAndDeletedFalse(Long id);
}
