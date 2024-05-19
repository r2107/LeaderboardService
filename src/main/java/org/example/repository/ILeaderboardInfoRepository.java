package org.example.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.LeaderboardScore;
import org.example.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface ILeaderboardInfoRepository extends JpaRepository<LeaderboardScore, Integer> {

//    Optional<LeaderboardScore> findByPlayerId(String playerId);

//    @Query(value = "SELECT * FROM leaderboard_scores WHERE leaderboard_id = ?1 ORDER BY score DESC LIMIT ?2", nativeQuery = true)
//    List<Pla> findTopKByLeaderboardIdOrderByScoreDesc(String leaderboardId, long k);

}
