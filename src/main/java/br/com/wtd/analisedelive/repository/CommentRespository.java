package br.com.wtd.analisedelive.repository;

import br.com.wtd.analisedelive.model.CommentsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRespository extends JpaRepository<CommentsInfo, String> {
}
