package com.cooksys.socialMediaApi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.socialMediaApi.entities.Tweet;

// TODO: Add Tweet entity import
@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
	
	List<Tweet> findByDeletedFalseOrderByPostedDesc();
	
}
