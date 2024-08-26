package com.cooksys.socialMediaApi.entities;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class HashTag {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true, nullable = false)
	private String label;

	@OneToMany(mappedBy = "hashTag")
	private List<tweetHashtag> tweetHashtags;

	private Timestamp firstUsed;

	private Timestamp lastUsed;

	@PrePersist
	protected void onCreate() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		this.firstUsed = now;
		this.lastUsed = now;
	}

	@PreUpdate
	protected void onUpdate() {
		this.lastUsed = new Timestamp(System.currentTimeMillis());
	}
}
