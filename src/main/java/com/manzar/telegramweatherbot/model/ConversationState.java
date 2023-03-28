package com.manzar.telegramweatherbot.model;

/**
 * Represents conversation state between bot and user.
 */
public enum ConversationState {
  CONVERSATION_STARTED, WAITING_FOR_CITY, WAITING_FOR_DATE,
  WAITING_FOR_NOTIFICATION_TYPE, WAITING_FOR_NOTIFICATION_TIME,
  WAITING_FOR_LANGUAGE, WAITING_FOR_UNIT_SYSTEM
}
