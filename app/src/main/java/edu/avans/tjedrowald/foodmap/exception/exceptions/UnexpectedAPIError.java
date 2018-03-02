package edu.avans.tjedrowald.foodmap.exception.exceptions;

public class UnexpectedAPIError extends YelpFusionError {
    public UnexpectedAPIError(int responseCode, String message) {
        this(responseCode, message, null, null);
    }

    public UnexpectedAPIError(int responseCode, String message, String code, String description) {
        super(responseCode, message, code, description);
    }
}
