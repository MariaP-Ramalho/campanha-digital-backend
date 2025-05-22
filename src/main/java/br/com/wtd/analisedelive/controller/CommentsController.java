package br.com.wtd.analisedelive.controller;

import br.com.wtd.analisedelive.model.CommentsInfo;
import br.com.wtd.analisedelive.repository.CommentsRepository;
import br.com.wtd.analisedelive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentsController {
    @Autowired
    private CommentsRepository commentsRepo;
    @Autowired private UserRepository userRepo;

    @GetMapping("/{liveId}")
    public List<CommentsInfo> getByLive(@PathVariable String liveId,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        var user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        return commentsRepo.findByLive_LiveIdAndUser_Id(liveId, user.getId());
    }
}

