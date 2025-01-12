package com.cooksys.socialMediaApi.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true, nullable = false)
	private String label;

	@ManyToMany(mappedBy="hashtags", cascade=CascadeType.ALL)
	private List<Tweet> tweets;

	@CreationTimestamp
	private Timestamp firstUsed;

	@UpdateTimestamp
	private Timestamp lastUsed;

}
