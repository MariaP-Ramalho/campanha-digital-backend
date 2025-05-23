package br.com.wtd.analisedelive.controller;

import br.com.wtd.analisedelive.model.Live;
import br.com.wtd.analisedelive.model.User;
import br.com.wtd.analisedelive.repository.LiveRepository;
import br.com.wtd.analisedelive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/lives")
public class LiveController {
    @Autowired
    private LiveRepository liveRepo;
    @Autowired private UserRepository userRepo;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LiveRepository liveRepository;

    @GetMapping
    public List<Live> listarLives(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(" Usuário autenticado: " + userDetails.getUsername());
        var user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        return liveRepo.findByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<String> adicionarLive(@RequestBody Live novaLive,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("Usuário autenticado no /lives: " + userDetails);
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }
        var user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        novaLive.setUser(user);
        liveRepo.save(novaLive);
        return ResponseEntity.ok("Live adicionada com sucesso");
    }


    @DeleteMapping("/{liveId}")
    public ResponseEntity<Void> deleteLive(@PathVariable String liveId, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Live> live = liveRepository.findByLiveIdAndUserId(liveId, getUserId(userDetails));
        if (live.isPresent()) {
            liveRepository.delete(live.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{liveId}")
    public ResponseEntity<Void> updateLiveTitle(@PathVariable String liveId, @RequestBody Map<String, String> body, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Live> liveOpt = liveRepository.findByLiveIdAndUserId(liveId, getUserId(userDetails));
        if (liveOpt.isPresent()) {
            Live live = liveOpt.get();
            live.setTitle(body.get("title"));
            liveRepository.save(live);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private Long getUserId(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}