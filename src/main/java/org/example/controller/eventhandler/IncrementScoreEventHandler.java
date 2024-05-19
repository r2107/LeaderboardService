package org.example.controller.eventhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.AddScoreEventInput;
import org.example.service.LeaderboardService;
import org.example.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.apache.commons.lang3.Validate;

public class IncrementScoreEventHandler {

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private ObjectMapper objectMapper;

    public void processEvent(@NonNull final String message) {
        Validate.notEmpty(message);
        final AddScoreEventInput addScoreEventInput = ObjectMapperUtils.deSerialize(message, AddScoreEventInput.class, objectMapper);
        Validate.notNull(addScoreEventInput);
        leaderboardService.incrementScore(addScoreEventInput);
    }
}
