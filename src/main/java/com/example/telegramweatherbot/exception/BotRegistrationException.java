package com.example.telegramweatherbot.exception;

/**
 * Exception for bot registration failures.
 */
public class BotRegistrationException extends RuntimeException {

  public BotRegistrationException(String message) {
    super(message);
  }

  public BotRegistrationException(String message, Throwable cause) {
    super(message, cause);
  }
}
