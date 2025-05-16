package br.com.wtd.analisedelive.main;

import br.com.wtd.analisedelive.model.CommentsInfo;
import br.com.wtd.analisedelive.model.CommentsInfoData;
import br.com.wtd.analisedelive.model.GeneralInfoData;
import br.com.wtd.analisedelive.repository.CommentRespository;
import br.com.wtd.analisedelive.service.CheckLiveActivity;
import br.com.wtd.analisedelive.service.ConsumeApi;
import br.com.wtd.analisedelive.service.ConvertData;
import br.com.wtd.analisedelive.service.FetchLiveComments;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.HttpUrl;

import java.sql.SQLOutput;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    private final CheckLiveActivity checkLive = new CheckLiveActivity();
    private final FetchLiveComments fetchLiveComments = new FetchLiveComments();
    private final Dotenv dotenv = Dotenv.load();
    private final String VIDEO_ID = dotenv.get("VIDEO_ID");
    private final String YOUTUBE_API_KEY = dotenv.get("YOUTUBE_API_KEY");
    private final String SHEET_ID = dotenv.get("SHEET_ID");
    private final CommentRespository repository;
    private final ConsumeApi consume = new ConsumeApi();
    private final ConvertData converter = new ConvertData();

    public Main(CommentRespository repository) {
        this.repository = repository;
    }


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
        System.out.println("fetch live comments" + json);
        GeneralInfoData generalInfoData = converter.getData(json, GeneralInfoData.class);
        processComments(generalInfoData);
    }

    public void processComments(GeneralInfoData generalInfoData) {
        System.out.println("Entrou no processamento");
        System.out.println("Comentários recebidos: " + generalInfoData.commentsInfo());
        for(CommentsInfoData commentsInfoData : generalInfoData.commentsInfo()){
            System.out.println("entrou no for");
            CommentsInfo commentsInfo = new CommentsInfo(
                    commentsInfoData.commentId(),
                    commentsInfoData.commentsDetail(),
                    commentsInfoData.authorDetails()
            );

            try{
                repository.save(commentsInfo);
                System.out.println("Comentário Salvo: " + commentsInfo);
            }catch (Exception e){
                System.out.println("Erro ao Salvar: " + e.getMessage());
            }
        }
    }

}
