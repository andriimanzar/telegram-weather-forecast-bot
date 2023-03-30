package com.manzar.telegramweatherbot.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.manzar.telegramweatherbot.keyboard.LanguageKeyboardBuilder;
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
class LanguageHandlerTest {

  @Mock
  private MessageSendingService messageSendingService;
  @Mock
  private UserSessionService userSessionService;
  @Mock
  private LanguageKeyboardBuilder languageKeyboardBuilder;
  @InjectMocks
  private LanguageHandler languageHandler;

  @Test
  void handleSendsChooseLanguageMessageWithPossibleLanguagesKeyboard() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED);

    languageHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userRequest.getUserSession(), "choose.language",
            languageKeyboardBuilder.build());
  }

  @Test
  void handleChangesSessionStateToWaitingForLanguage() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED);
    UserSession userSession = userRequest.getUserSession();

    languageHandler.handle(userRequest);

    assertEquals(ConversationState.WAITING_FOR_LANGUAGE, userSession.getConversationState());
    verify(userSessionService, times(1)).editUserSession(userSession);
  }
}