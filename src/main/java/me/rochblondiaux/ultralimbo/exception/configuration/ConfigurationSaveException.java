package me.rochblondiaux.ultralimbo.exception.configuration;

public class ConfigurationSaveException extends RuntimeException {

    public ConfigurationSaveException(String message) {
        super(message);
    }

    public ConfigurationSaveException(String message, Throwable cause) {
        super(message, cause);
    }

}
