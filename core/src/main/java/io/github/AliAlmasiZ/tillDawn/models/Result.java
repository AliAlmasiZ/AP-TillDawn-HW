package io.github.AliAlmasiZ.tillDawn.models;

public class Result {
    private final String message;
    private final boolean isSuccessful;

    public Result(boolean isSuccessful, String message) {
        this.message = message;
        this.isSuccessful = isSuccessful;
    }

    public String message() {
        return message;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
