package org.example.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class RankedPlayer {

    @NonNull
    private Player player;

    @NonNull
    private Long globalRank;

}
