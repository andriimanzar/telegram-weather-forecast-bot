package com.manzar.telegramweatherbot.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.manzar.telegramweatherbot.keyboard.CancelKeyboardBuilder;
import com.manzar.telegramweatherbot.keyboard.ChangeCityKeyboardBuilder;
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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShowForecastHandlerTest {

  @Mock
  private MessageSendingService messageSendingService;
  @Mock
  private UserSessionService userSessionService;
  @Mock
  private ChangeCityKeyboardBuilder changeCityKeyboardBuilder;
  @Mock
  private CancelKeyboardBuilder cancelKeyboardBuilder;
  @InjectMocks
  private ShowForecastHandler showForecastHandler;

  @Test
  void handleSendsEnterCityMessageIfUserSessionCityIsNull() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED);
    UserSession userSession = userRequest.getUserSession();
    userSession.setCity(null);

    showForecastHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userSession, "enter.city", cancelKeyboardBuilder.build());
  }

  @Test
  void handleChangesSessionStateToWaitingForCityIfUserSessionCityIsNull() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED);
    UserSession userSession = userRequest.getUserSession();
    userSession.setCity(null);

    showForecastHandler.handle(userRequest);

    assertEquals(ConversationState.WAITING_FOR_CITY,
        userSession.getConversationState());
    verify(userSessionService, times(1)).editUserSession(userSession);
  }

  @Test
  void handleSendsEnterDateMessageAndBuildsChangeCityKeyboardIfUserSessionCityIsNotNull() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED);
    UserSession userSession = userRequest.getUserSession();

    showForecastHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userSession, "enter.date", changeCityKeyboardBuilder.build());
  }

  @Test
  void handleChangesSessionStateToWaitingForDateIfUserSessionCityIsNotNull() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED);
    UserSession userSession = userRequest.getUserSession();

    showForecastHandler.handle(userRequest);

    assertEquals(ConversationState.WAITING_FOR_DATE,
        userSession.getConversationState());
    verify(userSessionService, times(1)).editUserSession(userSession);
  }
}