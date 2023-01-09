package com.manzar.telegramweatherbot.exception;

/**
 * Exception for unsuccessful reply-message sending.
 */
public class MessageSendingException extends RuntimeException {

  public MessageSendingException(String message) {
    super(message);
  }
}
