package com.manzar.telegramweatherbot.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.service.factory.UserRequestFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChangeCityHandlerTest {

  @Mock
  private MessageSendingService messageSendingService;
  @Mock
  private UserSessionService userSessionService;
  @InjectMocks
  private ChangeCityHandler changeCityHandler;

  @Test
  void handleSendsEnterCityMessage() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED);

    changeCityHandler.handle(userRequest);

    Mockito.verify(messageSendingService, Mockito.times(1))
        .sendMessage(userRequest.getUserSession(), "enter.city");
  }

  @Test
  void handleChangesSessionStateToWaitingForCity() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED);
    UserSession userSession = userRequest.getUserSession();

    changeCityHandler.handle(userRequest);

    assertEquals(ConversationState.WAITING_FOR_CITY, userSession.getConversationState());
    Mockito.verify(userSessionService, Mockito.times(1)).editUserSession(userSession);
  }

}