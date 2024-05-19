package org.example.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class Leaderboard {
    @NonNull
    private String leaderboardId;
    @NonNull
    private List<RankedPlayer> playersOnLeaderboard;
}
