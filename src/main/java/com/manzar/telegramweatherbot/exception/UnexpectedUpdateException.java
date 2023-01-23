package com.manzar.telegramweatherbot.exception;

/**
 * Exception for unexpected updates from user.
 */
public class UnexpectedUpdateException extends RuntimeException {

  public UnexpectedUpdateException(String message) {
    super(message);
  }
}
