package edu.avans.tjedrowald.foodmap.sync.exception.exceptions;

import java.io.IOException;

public abstract class YelpFusionError extends IOException {
    private String message;
    private String code;
    private String description;
    private int responseCode;

    public String getCode() {
        return code;
    }

    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public YelpFusionError(int responseCode, String message, String code, String description) {
        this.responseCode = responseCode;
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
