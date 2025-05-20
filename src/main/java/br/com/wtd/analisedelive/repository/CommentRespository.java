package br.com.wtd.analisedelive.repository;

import br.com.wtd.analisedelive.model.CommentsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRespository extends JpaRepository<CommentsInfo, String> {
    List<CommentsInfo> findByLiveVideoId(String liveVideoId);

}
