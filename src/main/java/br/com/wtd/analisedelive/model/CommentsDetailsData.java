package br.com.wtd.analisedelive.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommentsDetailsData(@JsonAlias("liveChatId") String commentLiveId,
                                  @JsonAlias("publishedAt") Timestamp commentTimeStamp,
                                  @JsonAlias("displayMessage") String commentContent) {
}
