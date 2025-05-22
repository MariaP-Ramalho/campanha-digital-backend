package br.com.wtd.analisedelive.model;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class CommentsInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private Live live;

    @Column(unique = true)
    private String ytCommentId;
    @Embedded
    private CommentsDetailsData commentsDetailsData;
    @Embedded
    private AuthorDetailsData authorDetailsData;

    @Enumerated(EnumType.STRING)
    private Sentiment sentiment;

    @Enumerated(EnumType.STRING)
    private Interaction interaction;

    private String liveVideoId;

    public CommentsInfo(){}

    public CommentsInfo(String commentId, CommentsDetailsData commentsDetailsData, AuthorDetailsData authorDetailsData, Sentiment sentiment, Interaction interaction) {
        this.ytCommentId = commentId;
        this.commentsDetailsData = commentsDetailsData;
        this.authorDetailsData = authorDetailsData;
        this.sentiment = sentiment;
        this.interaction = interaction;
    }

    public CommentsInfo(String commentId, CommentsDetailsData commentsDetailsData, AuthorDetailsData authorDetailsData) {
        this.ytCommentId = commentId;
        this.commentsDetailsData = commentsDetailsData;
        this.authorDetailsData = authorDetailsData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYtCommentId() {
        return ytCommentId;
    }

    public void setYtCommentId(String commentId) {
        this.ytCommentId = commentId;
    }

    public CommentsDetailsData getCommentsDetailsData() {
        return commentsDetailsData;
    }

    public void setCommentsDetailsData(CommentsDetailsData commentsDetailsData) {
        this.commentsDetailsData = commentsDetailsData;
    }

    public AuthorDetailsData getAuthorDetailsData() {
        return authorDetailsData;
    }

    public void setAuthorDetailsData(AuthorDetailsData authorDetailsData) {
        this.authorDetailsData = authorDetailsData;
    }

    public Sentiment getSentiment() {
        return sentiment;
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    public String getLiveVideoId() {
        return liveVideoId;
    }

    public void setLiveVideoId(String liveVideoId) {
        this.liveVideoId = liveVideoId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Live getLive() {
        return live;
    }

    public void setLive(Live live) {
        this.live = live;
    }
}
