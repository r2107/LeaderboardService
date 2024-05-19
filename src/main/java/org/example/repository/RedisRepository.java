package org.example.repository;

import lombok.NonNull;
import org.example.model.Player;

import java.util.List;
import java.util.Optional;

public interface RedisRepository {

    List<Player> getLeaderboard(@NonNull String leaderboardId,
                                @NonNull Long leaderboardSize);

    Boolean isKeyExistInRedisTemplate(@NonNull String leaderboardId);

    Double incrementScore(@NonNull String leaderboardId,
                          @NonNull String playerId,
                          @NonNull Double scoreToAdd);

    Optional<Long> getPlayerRank(@NonNull String leaderboardId,
                                 @NonNull String playerId);
}