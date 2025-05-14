package br.com.wtd.analisedelive.main;

import br.com.wtd.analisedelive.service.ConsumeApi;
import br.com.wtd.analisedelive.service.ConvertData;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    private final ConsumeApi consume = new ConsumeApi();
    private final ConvertData converter = new ConvertData();
    private final Dotenv dotenv = Dotenv.load();
    private final String OPENAI_API_KEY = dotenv.get("OPENAI_API_KEY");
    private final String VIDEO_ID = dotenv.get("VIDEO_ID");
    private final String YOUTUBE_API_KEY = dotenv.get("YOUTUBE_API_KEY");
    private final String SHEET_ID = dotenv.get("SHEET_ID");

    private final String url = "https://www.googleapis.com/youtube/v3/videos?part=liveStreamingDetails'&id="+ VIDEO_ID+ "&key=" + YOUTUBE_API_KEY;

    public void runMain(){
        System.out.println(url);
        var json = consume.getData(url);
        System.out.println(json);
    }

}
