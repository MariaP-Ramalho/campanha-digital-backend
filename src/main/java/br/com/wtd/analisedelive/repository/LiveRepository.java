package br.com.wtd.analisedelive.repository;

import br.com.wtd.analisedelive.model.Live;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveRepository extends JpaRepository<Live, Long> {
    List<Live> findByUserId(Long userId);
    Optional<Live> findByLiveIdAndUserId(String liveId, Long userId);
}

