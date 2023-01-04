package com.example.telegramweatherbot.handler;

import com.example.telegramweatherbot.model.UserRequest;
import java.util.List;

/**
 * Central handler, that decides to which of the handlers message from the user should go and
 * forwards the requests to one of them.
 */
public class DispatcherHandler {

  private final List<UserRequestHandler> handlers;

  public DispatcherHandler(List<UserRequestHandler> handlers) {
    this.handlers = handlers;
  }

  /**
   * Dispatches user request to one of the handlers.
   *
   * @return true if is any applicable user request handler, otherwise - false.
   */
  public boolean dispatch(UserRequest userRequest) {
    for (UserRequestHandler userRequestHandler : handlers) {
      if (userRequestHandler.isApplicable(userRequest)) {
        userRequestHandler.handle(userRequest);
        return true;
      }
    }
    return false;
  }
}
