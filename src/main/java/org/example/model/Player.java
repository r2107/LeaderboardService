package org.example.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.Serializable;

@Builder
@Value
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @NonNull
    private String id;

    @NonNull
    private Double score;

}