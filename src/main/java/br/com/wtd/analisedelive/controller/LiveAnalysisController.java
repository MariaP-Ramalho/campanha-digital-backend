package br.com.wtd.analisedelive.controller;

import br.com.wtd.analisedelive.service.LiveAnalysisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/live")
public class LiveAnalysisController {

    @Autowired
    private LiveAnalysisManager manager;

    @PostMapping("/start/{liveId}")
    public ResponseEntity<String> start(@PathVariable String liveId) {
        boolean started = manager.startAnalysis(liveId);
        return started
                ? ResponseEntity.ok("Análise iniciada para a live: " + liveId)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao iniciar análise.");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stop() {
        manager.stopAnalysis();
        return ResponseEntity.ok("Análise interrompida.");
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return manager.isRunning()
                ? ResponseEntity.ok("Análise em execução.")
                : ResponseEntity.ok("Análise parada.");
    }
}

