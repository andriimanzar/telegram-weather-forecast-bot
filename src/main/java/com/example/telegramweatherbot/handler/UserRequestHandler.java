package com.example.telegramweatherbot.handler;

import com.example.telegramweatherbot.model.UserRequest;

/**
 * Declares methods, that used in every user requests handlers.
 */
public interface UserRequestHandler {

  boolean isApplicable(UserRequest request);

  void handle(UserRequest requestToDispatch);

  boolean isGlobal();


}
