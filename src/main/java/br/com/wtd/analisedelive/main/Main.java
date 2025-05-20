package br.com.wtd.analisedelive.main;

import br.com.wtd.analisedelive.model.CommentsInfo;
import br.com.wtd.analisedelive.model.CommentsInfoData;
import br.com.wtd.analisedelive.model.GeneralInfoData;
import br.com.wtd.analisedelive.repository.CommentRespository;
import br.com.wtd.analisedelive.service.CheckLiveActivity;
import br.com.wtd.analisedelive.service.ConvertData;
import br.com.wtd.analisedelive.service.FetchLiveComments;
import br.com.wtd.analisedelive.service.OpenAIAnalysis;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final CheckLiveActivity checkLive = new CheckLiveActivity();
    private final FetchLiveComments fetchLiveComments = new FetchLiveComments();
    private final CommentRespository repository;
    private final ConvertData converter = new ConvertData();
    private final OpenAIAnalysis openAIAnalysis;

    public Main(CommentRespository repository, OpenAIAnalysis openAIAnalysis) {
        this.repository = repository;
        this.openAIAnalysis = openAIAnalysis;
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
        GeneralInfoData generalInfoData = converter.getData(json, GeneralInfoData.class);
        System.out.println("TOTAL DE COMENTÁRIOS: " + fetchLiveComments.getTotalResults());
        processComments(generalInfoData);
    }

    public void processComments(GeneralInfoData generalInfoData) {
        List<CommentsInfo> allComments = new ArrayList<>();

        for (CommentsInfoData commentsInfoData : generalInfoData.commentsInfo()) {
            CommentsInfo comment = new CommentsInfo(
                    commentsInfoData.commentId(),
                    commentsInfoData.commentsDetail(),
                    commentsInfoData.authorDetails()
            );
            allComments.add(comment);
        }

        openAIAnalysis.analyzeCommentsBatch(allComments);

        for (CommentsInfo comment : allComments) {
            try {
                System.out.println("entrou no try catch");
                repository.save(comment);
                System.out.println("Comentário Salvo: " + comment);
            } catch (Exception e) {
                System.out.println("Erro ao salvar comentário: " + e.getMessage());
            }
        }
    }

}
