package br.com.wtd.analisedelive.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "lives", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"live_id", "user_id"})
})
public class Live {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String liveId;
    private String title;

    @ManyToOne
    private User user;

    @ManyToOne
    private Tag tag;

    @OneToMany(mappedBy = "live", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CommentsInfo> comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CommentsInfo> getComments() {
        return comments;
    }

    public void setComments(List<CommentsInfo> comments) {
        this.comments = comments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}