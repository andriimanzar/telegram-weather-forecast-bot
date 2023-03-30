package com.manzar.telegramweatherbot.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.manzar.telegramweatherbot.keyboard.StartMenuKeyboardBuilder;
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
class CancelHandlerTest {

  @Mock
  private MessageSendingService messageSendingService;
  @Mock
  private UserSessionService userSessionService;
  @Mock
  private StartMenuKeyboardBuilder startMenuKeyboardBuilder;
  @InjectMocks
  private CancelHandler cancelHandler;

  @Test
  void handleSendsCancelMessageWithStartKeyboard() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.WAITING_FOR_CITY);

    cancelHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userRequest.getUserSession(), "cancel", startMenuKeyboardBuilder.build());
  }

  @Test
  void handleChangesSessionStateToConversationStarted() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.WAITING_FOR_CITY);
    UserSession userSession = userRequest.getUserSession();

    cancelHandler.handle(userRequest);

    assertEquals(ConversationState.CONVERSATION_STARTED, userSession.getConversationState());
    verify(userSessionService, times(1)).editUserSession(userSession);
  }

}