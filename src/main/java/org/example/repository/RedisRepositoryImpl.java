package org.example.repository;

import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.exceptions.ResourceNotFoundException;
import org.example.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

    private RedisTemplate<String, String> redisTemplate;
    private ZSetOperations<String, String> sortedSetOperations;
    private HashOperations<String, String, String> hashOperations;
    private static final int TIME_OF_EXPIRY_IN_DAYS = 30;

    @Autowired
    public RedisRepositoryImpl(final RedisTemplate<String, String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        sortedSetOperations = redisTemplate.opsForZSet();
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public List<Player> getLeaderboard(@NonNull final String leaderboardId,
                                       @NonNull final Long leaderboardSize) {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(leaderboardId))) {
            throw new ResourceNotFoundException(String.format("Leaderboard with id: %s doesn't exists", leaderboardId));
        }
        final Set<ZSetOperations.TypedTuple<String>> playerTuples =
                sortedSetOperations.rangeWithScores(leaderboardId, NumberUtils.INTEGER_ZERO, leaderboardSize - 1);
        return playerTuples.stream()
                .map(playerTuple -> Player.builder()
                        .id(playerTuple.getValue())
                        .score(playerTuple.getScore())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Boolean isKeyExistInRedisTemplate(@NonNull final String leaderboardId) {
        return redisTemplate.hasKey(leaderboardId);
    }

    @Override
    public Double incrementScore(@NonNull final String leaderboardId,
                                 @NonNull final String playerId,
                                 @NonNull final Double scoreToAdd) {
        if (Objects.equals(sortedSetOperations.size(leaderboardId), NumberUtils.LONG_ZERO)) {
            redisTemplate.expire(leaderboardId, TIME_OF_EXPIRY_IN_DAYS, TimeUnit.DAYS);
        }
        return sortedSetOperations.incrementScore(leaderboardId, playerId, scoreToAdd);
    }

    @Override
    public Optional<Long> getPlayerRank(@NonNull final String leaderboardId,
                                        @NonNull final String playerId) {
        return Optional.ofNullable(sortedSetOperations.rank(leaderboardId, playerId));
    }

}