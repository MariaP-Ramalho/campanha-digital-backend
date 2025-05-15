package br.com.wtd.analisedelive.service;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.HttpUrl;

import java.util.Objects;

public class FetchLiveComments {
    private final Dotenv dotenv = Dotenv.load();
    private final String YOUTUBE_API_KEY = dotenv.get("YOUTUBE_API_KEY");
    private final ConsumeApi consume = new ConsumeApi();

    public String fetchLiveComments(String activeChatId) {
        String lastPageToken = null;

        HttpUrl url = Objects.requireNonNull(HttpUrl.parse("https://www.googleapis.com/youtube/v3/liveChat/messages\n"))
                .newBuilder()
                .addQueryParameter("liveChatId", activeChatId)
                .addQueryParameter("part", "snippet,authorDetails")
                .addQueryParameter("key", YOUTUBE_API_KEY)
                .addQueryParameter("maxResults", "200")
                .build();

        return consume.getData(String.valueOf(url));
    }
}
