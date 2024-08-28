package com.cooksys.socialMediaApi.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	private User author;

	@CreationTimestamp
	private Timestamp posted;

	private boolean deleted = false;

	private String content;

	@ManyToOne
	private Tweet inReplyTo;

	@ManyToOne
	private Tweet repostOf;

	@ManyToMany
	@JoinTable(
			name = "tweet_hashtags", 
			joinColumns = @JoinColumn(name = "tweet_id"), 
			inverseJoinColumns = @JoinColumn(name = "hashtag_id")
			)
	private List<Hashtag> hashtags;

	@OneToMany(mappedBy = "inReplyTo")
	private List<Tweet> replies;

	@OneToMany(mappedBy = "repostOf")
	private List<Tweet> reposts;

	@ManyToMany
	@JoinTable(
			name = "user_mentions", 
			joinColumns = @JoinColumn(name = "tweet_id"), 
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> mentionedUsers;

	@ManyToMany(mappedBy = "likedTweets")
    private List<User> likedByUsers;
}