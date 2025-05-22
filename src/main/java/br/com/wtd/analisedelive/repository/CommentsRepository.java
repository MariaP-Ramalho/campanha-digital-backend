package br.com.wtd.analisedelive.repository;

import br.com.wtd.analisedelive.model.CommentsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<CommentsInfo, Long> {
    List<CommentsInfo> findByLive_LiveIdAndUser_Id(String liveId, Long userId);
}
