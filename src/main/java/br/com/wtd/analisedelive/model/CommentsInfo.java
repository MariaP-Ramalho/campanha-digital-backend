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

    public CommentsInfo(){}

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
}
