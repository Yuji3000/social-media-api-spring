package com.cooksys.socialMediaApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TODO: Add Tweet entity import
@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
