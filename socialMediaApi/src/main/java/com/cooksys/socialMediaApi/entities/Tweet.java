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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Tweet {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "author")
	private User user;

	@Column(nullable = false)
	private Timestamp posted;

	@Column(nullable = false)
	private Boolean deleted = false;

	@Column(nullable = true)
	private String content;

	@ManyToOne
	@JoinColumn(name = "in_reply_to")
	private Tweet inReplyTo;

	@ManyToOne
	@JoinColumn(name = "repost_of")
	private Tweet repostOf;

	@ManyToMany
	@JoinTable(name = "tweet_hashtags", joinColumns = @JoinColumn(name = "tweet_id"), inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
	private List<Hashtag> hashtags;

	@OneToMany(mappedBy = "inReplyTo")
	private List<Tweet> replies;

	@OneToMany(mappedBy = "repostOf")
	private List<Tweet> reposts;

	@ManyToMany
	@JoinTable(name = "user_mentions", joinColumns = @JoinColumn(name = "tweet_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<Tweet> mentions;

	@PrePersist
	protected void onCreate() {
		this.posted = new Timestamp(System.currentTimeMillis());
	}
}