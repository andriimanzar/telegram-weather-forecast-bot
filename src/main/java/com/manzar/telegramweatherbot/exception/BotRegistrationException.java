package com.manzar.telegramweatherbot.exception;

/**
 * Exception for bot registration failures.
 */
public class BotRegistrationException extends RuntimeException {

  public BotRegistrationException(String message, Throwable cause) {
    super(message, cause);
  }
}
