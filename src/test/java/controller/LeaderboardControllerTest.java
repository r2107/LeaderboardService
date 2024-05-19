package controller;

import com.google.common.collect.ImmutableList;
import org.example.controller.LeaderboardController;
import org.example.dto.AddScoreEventInput;
import org.example.dto.LeaderboardDTO;
import org.example.mapper.LeaderboardConvertor;
import org.example.model.Leaderboard;
import org.example.service.LeaderboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LeaderboardControllerTest {

    @Mock
    private LeaderboardService leaderboardService;

    @Mock
    private LeaderboardConvertor leaderboardConvertor;

    @InjectMocks
    private LeaderboardController leaderboardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetLeaderboard() {
        // Mock LeaderboardService and LeaderboardConvertor behavior
        final String leaderboardId = "testLeaderboard";
        final LeaderboardDTO expectedLeaderboardDTO = LeaderboardDTO.builder().playersOnLeaderboard(ImmutableList.of()).build();
        when(leaderboardService.getLeaderboard(anyString(), anyLong()))
                .thenReturn(Leaderboard.builder().playersOnLeaderboard(ImmutableList.of()).leaderboardId(leaderboardId).build());
        when(leaderboardConvertor.convertToExternalLeaderboard(any())).thenReturn(expectedLeaderboardDTO);

        // Call the controller method
        final ResponseEntity<LeaderboardDTO> responseEntity = leaderboardController.leaderboard(leaderboardId, 5L);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedLeaderboardDTO, responseEntity.getBody());
    }

    @Test
    void testIncrementScore() {
        // Mock LeaderboardService behavior
        final AddScoreEventInput addScoreEventInput = AddScoreEventInput.builder().score(50.0).playerId("p1").leaderboardId("l1").build();
        doNothing().when(leaderboardService).incrementScore(any());

        // Call the controller method
        ResponseEntity<?> responseEntity = leaderboardController.incrementScore(addScoreEventInput);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }
}

