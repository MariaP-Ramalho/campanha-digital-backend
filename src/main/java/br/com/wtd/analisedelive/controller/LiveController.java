package br.com.wtd.analisedelive.controller;

import br.com.wtd.analisedelive.model.Live;
import br.com.wtd.analisedelive.repository.LiveRepository;
import br.com.wtd.analisedelive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lives")
public class LiveController {
    @Autowired
    private LiveRepository liveRepo;
    @Autowired private UserRepository userRepo;

    @GetMapping
    public List<Live> listarLives(@AuthenticationPrincipal UserDetails userDetails) {
        var user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        return liveRepo.findByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<String> adicionarLive(@RequestBody Live novaLive,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        var user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        novaLive.setUser(user);
        liveRepo.save(novaLive);
        return ResponseEntity.ok("Live adicionada com sucesso");
    }
}