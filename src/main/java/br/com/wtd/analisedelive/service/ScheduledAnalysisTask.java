package br.com.wtd.analisedelive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledAnalysisTask {
    @Autowired
    private LiveAnalysisManager manager;

    @Scheduled(fixedRate = 10 * 1000) // 5 minutos  5 * 60 * 1000
    public void runScheduledTask() {
        if (manager.isRunning()) {
            manager.executeAnalysis();
        }
    }
}
