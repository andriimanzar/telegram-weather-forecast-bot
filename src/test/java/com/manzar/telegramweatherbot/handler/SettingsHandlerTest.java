package com.manzar.telegramweatherbot.handler;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.manzar.telegramweatherbot.keyboard.SettingsKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.factory.UserRequestFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingsHandlerTest {

  @Mock
  private MessageSendingService messageSendingService;
  @Mock
  private SettingsKeyboardBuilder settingsKeyboardBuilder;
  @InjectMocks
  private SettingsHandler settingsHandler;

  @Test
  void handleSendsSettingsMessageAndBuildsSettingsKeyboard() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED);

    settingsHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userRequest.getUserSession(), "settings", settingsKeyboardBuilder.build());
  }
}

