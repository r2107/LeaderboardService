package service;

import com.google.common.collect.ImmutableList;
import org.example.dto.AddScoreEventInput;
import org.example.exceptions.ResourceNotFoundException;
import org.example.model.Leaderboard;
import org.example.model.LeaderboardScore;
import org.example.model.Player;
import org.example.model.RankedPlayer;
import org.example.repository.ILeaderboardInfoRepository;
import org.example.repository.RedisRepository;
import org.example.service.LeaderboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeaderboardServiceTest {

    @InjectMocks
    private LeaderboardService leaderboardService;

    @Mock
    private RedisRepository redisRepository;

    @Mock
    private ILeaderboardInfoRepository leaderboardInfoRepository;

    private static final List<Player> players = ImmutableList.of(
            getPlayer("player_1", 100.0),
            getPlayer("player_2", 99.0),
            getPlayer("player_3", 97.0),
            getPlayer("player_4", 53.0),
            getPlayer("player_5", 57.0),
            getPlayer("player_6", 64.0),
            getPlayer("player_7", 93.0),
            getPlayer("player_8", 90.0),
            getPlayer("player_9", 70.0),
            getPlayer("player_10", 82.0)
    );

    private static final List<RankedPlayer> rankedPlayers = ImmutableList.of(
            getRankedPlayer("player_1", 100.0, 1l),
            getRankedPlayer("player_2", 99.0, 2l),
            getRankedPlayer("player_3", 97.0, 3l),
            getRankedPlayer("player_4", 53.0, 10l),
            getRankedPlayer("player_5", 57.0, 9l),
            getRankedPlayer("player_6", 64.0, 8l),
            getRankedPlayer("player_7", 93.0, 4l),
            getRankedPlayer("player_8", 90.0, 5l),
            getRankedPlayer("player_9", 70.0, 7l),
            getRankedPlayer("player_10", 82.0, 6l)
    );

    private static Player getPlayer(final String playerId, final Double score) {
        return Player.builder().id(playerId).score(score).build();
    }

    private static RankedPlayer getRankedPlayer(final String playerId, final Double score, final Long rank) {
        return RankedPlayer.builder()
                .player(getPlayer(playerId, score))
                .globalRank(rank)
                .build();
    }

    private static final String LEADERBOARD_ID = "leaderboard_1";
    private static final String PLAYER_ID = "player_1";
    private static final double SCORE = 10.0;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testGetLeaderboard_returnsCorrectLeaderboard() {
        long leaderboardSize = 3;
        final Player player1 = getPlayer("player_1", 100.0);
        final Player player2 = getPlayer("player_2", 99.0);
        final Player player3 = getPlayer("player_3", 97.0);

        final RankedPlayer rankedPlayer1 = getRankedPlayer("player_1", 100.0, 1L);
        final RankedPlayer rankedPlayer2 = getRankedPlayer("player_1", 100.0, 1L);
        final RankedPlayer rankedPlayer3 = getRankedPlayer("player_1", 100.0, 1L);

        when(redisRepository.isKeyExistInRedisTemplate(LEADERBOARD_ID)).thenReturn(true);
        when(redisRepository.getLeaderboard(LEADERBOARD_ID, 3L)).thenReturn(ImmutableList.of(player1, player2, player3));
        when(redisRepository.getPlayerRank(LEADERBOARD_ID, "player_1")).thenReturn(Optional.of(0L));
        when(redisRepository.getPlayerRank(LEADERBOARD_ID, "player_2")).thenReturn(Optional.of(1L));
        when(redisRepository.getPlayerRank(LEADERBOARD_ID, "player_3")).thenReturn(Optional.of(2L));

        final Leaderboard expectedLeaderboard = Leaderboard.builder().leaderboardId(LEADERBOARD_ID)
                .playersOnLeaderboard(ImmutableList.of(rankedPlayer1, rankedPlayer2, rankedPlayer3)).build();

        // Test the method
        final Leaderboard actualLeaderboard = leaderboardService.getLeaderboard(LEADERBOARD_ID, leaderboardSize);

        // Verify the result
        assertNotNull(actualLeaderboard);
        assertEquals(LEADERBOARD_ID, actualLeaderboard.getLeaderboardId());
        assertEquals(3, actualLeaderboard.getPlayersOnLeaderboard().size());
        // add more asserts
    }

    @Test
    public void testGetLeaderboard_throwsResourceNotFoundException_whenLeaderboardDoesNotExist() {
        long leaderboardSize = 5;
        when(redisRepository.isKeyExistInRedisTemplate(LEADERBOARD_ID)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> leaderboardService.getLeaderboard(LEADERBOARD_ID, leaderboardSize));
    }

    @Test
    public void testIncrementScore_incrementsScoreAndSavesToLeaderboardInfoRepository() {
        when(redisRepository.incrementScore(LEADERBOARD_ID, PLAYER_ID, SCORE)).thenReturn(SCORE);
        leaderboardService.incrementScore(AddScoreEventInput.builder().leaderboardId(LEADERBOARD_ID).playerId(PLAYER_ID).score(SCORE).build());
        verify(leaderboardInfoRepository, times(1)).save(any(LeaderboardScore.class));
    }

}