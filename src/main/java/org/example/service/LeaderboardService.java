package org.example.service;

import io.netty.util.internal.StringUtil;
import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tomcat.util.buf.StringUtils;
import org.example.dto.AddScoreEventInput;
import org.example.exceptions.LeaderboardServiceNonRetryableException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.model.Leaderboard;
import org.example.model.LeaderboardScore;
import org.example.model.Player;
import org.example.model.RankedPlayer;
import org.example.repository.ILeaderboardInfoRepository;
import org.example.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LeaderboardService {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private ILeaderboardInfoRepository leaderboardInfoRepository;

    private static final String DELIMETER = "#";

    public Leaderboard getLeaderboard(@NonNull final String leaderboardId,
                                      @NonNull final Long leaderboardSize){
        if (!redisRepository.isKeyExistInRedisTemplate(leaderboardId)) {
            throw new ResourceNotFoundException(String.format("Leaderboard with id: %s doesn't exists", leaderboardId));
        }
        final List<Player> players = redisRepository
                .getLeaderboard(leaderboardId, leaderboardSize);
        final List<RankedPlayer> rankedPlayers = players.stream()
                .map(player -> RankedPlayer.builder()
                        .player(Player.builder()
                                .id(fetchWithoutTimestamp(player.getId()))
                                .score(player.getScore())
                                .build())
                        .globalRank(redisRepository.getPlayerRank(leaderboardId, player.getId())
                                .orElseThrow(() -> new LeaderboardServiceNonRetryableException(
                                        String.format("Player %s doesn't exists", player.getId()))) + NumberUtils.LONG_ONE)
                        .build())
                .sorted(Comparator.comparingLong(RankedPlayer::getGlobalRank))
                .collect(Collectors.toList());
        return Leaderboard.builder()
                .playersOnLeaderboard(rankedPlayers)
                .leaderboardId(leaderboardId).build();
    }

    public void incrementScore(@NonNull final AddScoreEventInput addScoreEventInput) {
        final String updatedPlayerIdWithTimestamp = concatenateWithCurrentTimestamp(addScoreEventInput.getPlayerId());
        redisRepository.incrementScore(addScoreEventInput.getLeaderboardId(), updatedPlayerIdWithTimestamp, addScoreEventInput.getScore());
        // Ideally, this operation can be made asynchronous.
        leaderboardInfoRepository.save(LeaderboardScore.builder()
                .leaderboardId(addScoreEventInput.getLeaderboardId())
                .playerId(addScoreEventInput.getPlayerId())
                .score(addScoreEventInput.getScore())
                .build());
    }

    // TODO: to move in some UTILs file
    private String concatenateWithCurrentTimestamp(final String playerId) {
        return Stream.of(System.currentTimeMillis(), playerId)
                .map(Object::toString)
                .collect(Collectors.joining(DELIMETER));
    }
    private String fetchWithoutTimestamp(final String key) {
        int index = key.indexOf(DELIMETER);
        if (index == 1) {
            throw new IllegalArgumentException(); // can be updated
        }
        return key.substring(index + 1);
    }

}
