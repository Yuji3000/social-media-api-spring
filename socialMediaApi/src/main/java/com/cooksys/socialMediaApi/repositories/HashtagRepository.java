package com.cooksys.socialMediaApi.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.socialMediaApi.entities.Hashtag;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    boolean existsByLabel(String label);

    Optional<Hashtag> findByLabel(String label);
}
