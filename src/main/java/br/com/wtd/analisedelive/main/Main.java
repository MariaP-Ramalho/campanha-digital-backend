package br.com.wtd.analisedelive.main;

import br.com.wtd.analisedelive.service.CheckLiveActivity;
import br.com.wtd.analisedelive.service.ConsumeApi;
import br.com.wtd.analisedelive.service.ConvertData;
import br.com.wtd.analisedelive.service.FetchLiveComments;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {
    private final CheckLiveActivity checkLive = new CheckLiveActivity();
    private final FetchLiveComments fetchLiveComments = new FetchLiveComments();
    private final Dotenv dotenv = Dotenv.load();
    private final String VIDEO_ID = dotenv.get("VIDEO_ID");
    private final String YOUTUBE_API_KEY = dotenv.get("YOUTUBE_API_KEY");
    private final String SHEET_ID = dotenv.get("SHEET_ID");

    private final String url = "https://www.googleapis.com/youtube/v3/videos?part=liveStreamingDetails&id="+ VIDEO_ID+ "&key=" + YOUTUBE_API_KEY;

    public void runMain() throws JsonProcessingException {

        String activeChatId = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insira o ID da live: ");
        String liveID = scanner.nextLine();

        try{
            activeChatId = checkLive.checkActivity(liveID);
            System.out.println("Live encontrada com sucesso!");
            System.out.println("Live ID: " + activeChatId);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("ID ATIVO: " + activeChatId);
        String json = fetchLiveComments.fetchLiveComments(activeChatId);
        System.out.println(json);
    }

}
