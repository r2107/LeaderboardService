package org.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LeaderboardServiceNonRetryableException extends RuntimeException {

    public LeaderboardServiceNonRetryableException() {
        super();
    }

    public LeaderboardServiceNonRetryableException(String message) {
        super(message);
    }

    public LeaderboardServiceNonRetryableException(String message, Throwable cause) {
        super(message, cause);
    }
}
