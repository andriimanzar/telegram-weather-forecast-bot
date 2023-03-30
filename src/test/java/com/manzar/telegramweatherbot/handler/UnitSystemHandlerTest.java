package com.manzar.telegramweatherbot.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.manzar.telegramweatherbot.keyboard.UnitSystemKeyboardBuilder;
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
class UnitSystemHandlerTest {

  @Mock
  private MessageSendingService messageSendingService;
  @Mock
  private UserSessionService userSessionService;
  @Mock
  private UnitSystemKeyboardBuilder unitSystemKeyboardBuilder;
  @InjectMocks
  private UnitSystemHandler unitSystemHandler;

  @Test
  void handleSendsChooseUnitSystemMessageAndBuildsKeyboard() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED);
    UserSession userSession = userRequest.getUserSession();

    unitSystemHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userSession, "choose.unit.system", unitSystemKeyboardBuilder.build());
  }

  @Test
  void handleChangesSessionStateToWaitingForUnitSystem() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED);
    UserSession userSession = userRequest.getUserSession();

    unitSystemHandler.handle(userRequest);

    assertEquals(ConversationState.WAITING_FOR_UNIT_SYSTEM,
        userSession.getConversationState());
    verify(userSessionService, times(1)).editUserSession(userSession);
  }


}