package me.rochblondiaux.ultralimbo.exception.configuration;

public class ConfigurationLoadException extends RuntimeException {

    public ConfigurationLoadException(String message) {
        super(message);
    }

    public ConfigurationLoadException(String message, Throwable cause) {
        super(message, cause);
    }

}
