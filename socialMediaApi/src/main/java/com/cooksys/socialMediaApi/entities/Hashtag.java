package com.cooksys.socialMediaApi.entities;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

	@ManyToMany
	@JoinTable(name = "tweet_hashtags", joinColumns = @JoinColumn(name = "hashtag_id"), inverseJoinColumns = @JoinColumn(name = "tweet_id"))
	private List<Hashtag> hashtags;

	@Column(nullable = false)
	private Timestamp firstUsed;

	@Column(nullable = false)
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
