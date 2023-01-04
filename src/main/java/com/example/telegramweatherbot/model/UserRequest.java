package com.example.telegramweatherbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Entity, that represents user request.
 */
@Getter
@Setter
@AllArgsConstructor
public class UserRequest {

  private Update update;
}
