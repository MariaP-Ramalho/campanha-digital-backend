package br.com.wtd.analisedelive.service;

import br.com.wtd.analisedelive.model.CommentsDetailsData;
import br.com.wtd.analisedelive.model.CommentsInfo;
import br.com.wtd.analisedelive.model.CommentsInfoData;
import br.com.wtd.analisedelive.model.GeneralInfoData;
import br.com.wtd.analisedelive.repository.CommentRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LiveAnalysisManager {

    private final CheckLiveActivity checkLive = new CheckLiveActivity();
    private final FetchLiveComments fetchLiveComments = new FetchLiveComments();
    private final CommentRespository repository;
    private final ConvertData converter = new ConvertData();
    private final OpenAIAnalysis openAIAnalysis;

    private String activeChatId = null;
    private String currentLiveId; // novo campo


    @Autowired
    public LiveAnalysisManager(CommentRespository repository, OpenAIAnalysis openAIAnalysis) {
        this.repository = repository;
        this.openAIAnalysis = openAIAnalysis;
    }

    public boolean startAnalysis(String liveID) {
        try {
            this.currentLiveId = liveID;
            this.activeChatId = checkLive.checkActivity(liveID);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao iniciar análise: " + e.getMessage());
            return false;
        }
    }


    public void executeAnalysis() {
        if (activeChatId == null) return;
        try {
            String json = fetchLiveComments.fetchLiveComments(activeChatId);
            GeneralInfoData generalInfoData = converter.getData(json, GeneralInfoData.class);
            processComments(generalInfoData);
        } catch (Exception e) {
            System.out.println("Erro durante análise: " + e.getMessage());
        }
    }

    private void processComments(GeneralInfoData generalInfoData) {
        List<CommentsInfo> allComments = new ArrayList<>();

        for (CommentsInfoData data : generalInfoData.commentsInfo()) {
            CommentsInfo comment = getCommentsInfo(data);

            allComments.add(comment);
        }
        
        openAIAnalysis.analyzeCommentsBatch(allComments);
        
        for (CommentsInfo comment : allComments) {
            try {
                repository.save(comment);
                System.out.println("Salvo com liveVideoId: " + comment.getLiveVideoId());
            } catch (Exception e) {
                System.out.println("Erro ao salvar comentário: " + e.getMessage());
            }
        }
    }

    private CommentsInfo getCommentsInfo(CommentsInfoData data) {
        CommentsInfo comment = new CommentsInfo(
                data.commentId(),
                data.commentsDetail(),
                data.authorDetails()
        );

        CommentsDetailsData oldDetails = comment.getCommentsDetailsData();
        CommentsDetailsData newDetails = new CommentsDetailsData(
                activeChatId,
                oldDetails.commentTimeStamp(),
                oldDetails.commentContent()
        );
        comment.setCommentsDetailsData(newDetails);

        comment.setLiveVideoId(currentLiveId);
        return comment;
    }


    public void stopAnalysis() {
        this.activeChatId = null;
    }

    public boolean isRunning() {
        return this.activeChatId != null;
    }
}
