package br.com.wtd.analisedelive.model;

import jakarta.persistence.*;

@Entity
@Table(name = "comments", uniqueConstraints = {
        @UniqueConstraint(columnNames = "comment_id")
})
public class CommentsInfo {
    @Id
    private String commentId;
    @Embedded
    private CommentsDetailsData commentsDetailsData;
    @Embedded
    private AuthorDetailsData authorDetailsData;

    @Enumerated(EnumType.STRING)
    private Sentiment sentiment;

    @Enumerated(EnumType.STRING)
    private Interaction interaction;

    public CommentsInfo(){}

    public CommentsInfo(String commentId, CommentsDetailsData commentsDetailsData, AuthorDetailsData authorDetailsData, Sentiment sentiment, Interaction interaction) {
        this.commentId = commentId;
        this.commentsDetailsData = commentsDetailsData;
        this.authorDetailsData = authorDetailsData;
        this.sentiment = sentiment;
        this.interaction = interaction;
    }

    public CommentsInfo(String commentId, CommentsDetailsData commentsDetailsData, AuthorDetailsData authorDetailsData) {
        this.commentId = commentId;
        this.commentsDetailsData = commentsDetailsData;
        this.authorDetailsData = authorDetailsData;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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
}
