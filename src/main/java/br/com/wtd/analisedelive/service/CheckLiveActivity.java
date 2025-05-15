package br.com.wtd.analisedelive.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

public class CheckLiveActivity {
    private final Dotenv dotenv = Dotenv.load();
    private final String YOUTUBE_API_KEY = dotenv.get("YOUTUBE_API_KEY");
    private final ConsumeApi consume = new ConsumeApi();

    public String checkActivity(String liveID) throws JsonProcessingException {
        String url = "https://www.googleapis.com/youtube/v3/videos?part=liveStreamingDetails&id="+ liveID + "&key=" + YOUTUBE_API_KEY;

        String json = consume.getData(url);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        JsonNode items = root.get("items");
        if (items == null || !items.isArray() || items.isEmpty()) {
            return "Vídeo não encontrado.";
        }

        JsonNode liveStreamingDetails = items.get(0).get("liveStreamingDetails");
        if (liveStreamingDetails == null || liveStreamingDetails.get("activeLiveChatId") == null) {
            return "Live Chat ID não encontrado. A live está realmente ativa?";
        }else{
            return "Live ativa!";
        }
    }
}

