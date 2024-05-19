package org.example.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class LeaderboardDTO {

    @NonNull
    private List<PlayerDTO> playersOnLeaderboard;

}
