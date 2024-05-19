package org.example.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class AddScoreEventInput {

    @NonNull
    private String leaderboardId;

    @NonNull
    private String playerId;

    @NonNull
    private Double score;

}
