package com.cooksys.socialMediaApi.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Data
@Table(name = "user_table")
public class User {

	@Id
	@GeneratedValue
	private Long id;

	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp joined;

	private boolean deleted = false;

	@ManyToMany
	@JoinTable(name = "followers_following")
	private List<User> following;

	@ManyToMany(mappedBy = "following")
	private List<User> followers;

	@ManyToMany
    @JoinTable(
            name = "user_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private List<Tweet> likedTweets;

	@ToString.Exclude
	@ManyToMany(mappedBy = "mentionedUsers")
    private List<Tweet> mentionedTweets;

	@OneToMany(mappedBy = "author")
	private List<Tweet> tweets;
	
	@Embedded
	private Profile profile;

	@Embedded
	private Credentials credentials;
}
