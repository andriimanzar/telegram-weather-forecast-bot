package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.model.UserRequest;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Central handler, that decides to which of the handlers message from the user should go and
 * forwards the requests to one of them.
 */
@Component
public class DispatcherHandler {

  private final List<UserRequestHandler> handlers;

  /**
   * Constructor that injects handlers into dispatcher, in reverse order, due to global handlers.
   */
  public DispatcherHandler(List<UserRequestHandler> handlers) {
    this.handlers = handlers.stream()
        .sorted(Comparator.comparing(UserRequestHandler::isGlobal).reversed())
        .collect(Collectors.toList());
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
