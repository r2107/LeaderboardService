package org.example.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.example.exceptions.NotificationProcessingException;

import java.io.IOException;

public final class ObjectMapperUtils {

    public static <T> T deSerialize(@NonNull final String content, @NonNull final Class<T> valueType,
                                    @NonNull final ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (IOException e) {
            throw new NotificationProcessingException(String.format("Failed to deserialize", e));
        }
    }

    public static <T> String serialize(@NonNull final T value, @NonNull final ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new NotificationProcessingException(String.format("Failed to serialize", e));
        }
    }
}
