package com.cooksys.socialMediaApi.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cooksys.socialMediaApi.entities.Tweet;


@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

	List<Tweet> findByDeletedFalseOrderByPostedDesc();

	Optional<Tweet> findByIdAndDeletedFalse(Long id);

	List<Tweet> findByDeletedFalseAndHashtagsLabelOrderByPostedDesc(String label);

	@Query("SELECT t FROM Tweet t WHERE (t.author.id = :userid OR t.author.id IN (SELECT u.id FROM User u JOIN u.followers f WHERE f.id = :userid)) AND t.deleted = false ORDER BY t.posted DESC")
	List<Tweet> getFeedByUserID(@Param("userid") Long id);
}
