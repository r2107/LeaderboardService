package org.example.mapper;

import lombok.NonNull;
import org.example.dto.LeaderboardDTO;
import org.example.dto.PlayerDTO;
import org.example.model.Leaderboard;
import org.example.model.RankedPlayer;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class LeaderboardConvertor {

    public LeaderboardDTO convertToExternalLeaderboard(@NonNull final Leaderboard leaderboard) {
        return LeaderboardDTO.builder()
                .playersOnLeaderboard(leaderboard.getPlayersOnLeaderboard().stream()
                        .map(this::convertToExternalPlayer)
                        .collect(Collectors.toList()))
                .build();
    }

    public PlayerDTO convertToExternalPlayer(@NonNull final RankedPlayer rankedPlayer) {
        return PlayerDTO.builder()
                .playerId(rankedPlayer.getPlayer().getId())
                .globalRank(rankedPlayer.getGlobalRank())
                .score(rankedPlayer.getPlayer().getScore())
                .build();
    }
}
