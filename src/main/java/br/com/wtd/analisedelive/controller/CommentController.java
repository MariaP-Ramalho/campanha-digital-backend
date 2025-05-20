package br.com.wtd.analisedelive.controller;

import br.com.wtd.analisedelive.model.CommentsInfo;
import br.com.wtd.analisedelive.repository.CommentRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentRespository repository;

    @GetMapping("/{liveId}")
    public List<CommentsInfo> getComentariosDaLive(@PathVariable String liveId) {
        return repository.findByLiveVideoId(liveId);
    }
}

