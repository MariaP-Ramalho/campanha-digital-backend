package br.com.wtd.analisedelive.service;

import br.com.wtd.analisedelive.model.*;
import br.com.wtd.analisedelive.repository.CommentsRepository;
import br.com.wtd.analisedelive.repository.LiveRepository;
import br.com.wtd.analisedelive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LiveAnalysisManager {
    private Long userId;
    private String liveId;
    private String activeChatId;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LiveRepository liveRepository;
    private final CheckLiveActivity checkLive = new CheckLiveActivity();
    private final FetchLiveComments fetchLiveComments = new FetchLiveComments();
    private final CommentsRepository repository;
    private final ConvertData converter = new ConvertData();
    private final OpenAIAnalysis openAIAnalysis;

    private String currentLiveId;

    private User user;
    private Live live;
    private boolean running;


    public void configureAnalysis(Long userId, String liveId) {
        this.user = userRepository.findById(userId).orElseThrow();
        this.live = liveRepository.findByLiveIdAndUserId(liveId, userId)
                .orElseThrow(() -> new RuntimeException("Live não encontrada para este usuário"));
    }

    @Autowired
    public LiveAnalysisManager(CommentsRepository repository, OpenAIAnalysis openAIAnalysis) {
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
        if (activeChatId == null || user == null || live == null) return;

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
            comment.setUser(user);
            comment.setLive(live);
            allComments.add(comment);
        }

        openAIAnalysis.analyzeCommentsBatch(allComments);
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
