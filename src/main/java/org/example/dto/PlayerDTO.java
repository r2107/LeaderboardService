package org.example.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class PlayerDTO {

    @NonNull
    private String playerId;

    @NonNull
    private Double score;

    @NonNull
    private Long globalRank;

}
