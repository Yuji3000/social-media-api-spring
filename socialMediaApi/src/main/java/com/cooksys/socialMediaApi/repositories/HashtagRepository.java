package com.cooksys.socialMediaApi.repositories;

import com.cooksys.socialMediaApi.entities.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<HashTag, Long> {
}
