package org.example.controller;

import org.example.dto.AddScoreEventInput;
import org.example.dto.LeaderboardDTO;
import org.example.mapper.LeaderboardConvertor;
import org.example.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping(value = "/leaderboard")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private LeaderboardConvertor leaderboardConvertor;

    /**
     * Retrieves the leaderboard data for the specified leaderboard ID.
     *
     * @param leaderboardId     The ID of the leaderboard to retrieve.
     * @param leaderboardSize   The number of entries to retrieve from the leaderboard (default: 5).
     * @return                  A ResponseEntity containing the leaderboard data in the form of a LeaderboardDTO.
     */
    @GetMapping("/{leaderboardId}")
    public ResponseEntity<LeaderboardDTO> leaderboard(
            @PathVariable(value = "leaderboardId") final String leaderboardId,
            @RequestParam(defaultValue = "5", value = "leaderboardSize", required = false) @Positive final Long leaderboardSize) {

        return new ResponseEntity<>(leaderboardConvertor.convertToExternalLeaderboard(
                leaderboardService.getLeaderboard(leaderboardId, leaderboardSize)), HttpStatus.OK);
    }

    @PostMapping("/increment-score")
    public ResponseEntity incrementScore(@RequestBody final AddScoreEventInput addScoreEventInput) {
        leaderboardService.incrementScore(addScoreEventInput);
        return ResponseEntity.ok().build();
    }

}
