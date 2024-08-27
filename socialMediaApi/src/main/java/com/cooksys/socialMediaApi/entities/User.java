package com.cooksys.socialMediaApi.entities;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "user_table")
public class User {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private Timestamp joined;

	@Column(nullable = false)
	private Boolean deleted = false;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String phone;

	@ManyToMany
	@JoinTable(name = "followers_following", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "following_id"))
	private List<User> following;

	@ManyToMany(mappedBy = "following")
	private List<User> followers;

	@ManyToMany
	@JoinTable(name = "user_likes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "tweet_id"))
	private List<Tweet> likedTweets;

	@ManyToMany
	@JoinTable(name = "user_mentions", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "tweet_id"))
	private List<Tweet> mentionedInTweets;

	@OneToMany(mappedBy = "user")
	private List<Tweet> tweets;

	@PrePersist
	protected void onCreate() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		this.joined = now;
	}
	
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "firstname", column = @Column(name = "profile_firstname")),
			@AttributeOverride(name = "lastname", column = @Column(name = "profile_lastname")),
			@AttributeOverride(name = "phone", column = @Column(name = "profile_phone")),
			@AttributeOverride(name = "email", column = @Column(name = "profile_email"))})
	
	private Profile profile;

}
