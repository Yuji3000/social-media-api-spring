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

	/**
	 * Find tweets that contain the desired hashtag and are not deleted.
	 * The "#" symbol is expected to be included in the input. This symbol
	 * will be appended to the input string by the caller.<p>
	 *
	 * It works by looking for a whole "word" like "#hashtag" and ignoring any
	 * punctuation that may be present. This allows ",#hashtag" or "#hashtag."
	 * to match. It also prevents something like "#hash,tag" or "#hash tag"
	 * from matching.
	 */
	@Query(value = "SELECT * FROM Tweet t WHERE t.deleted IS FALSE AND t.content ~* ('[^\\w\\s]*' || :label || '[^\\w\\s]*')", nativeQuery = true)
	public List<Tweet> getByHashtag(@Param("label") String label);

}
