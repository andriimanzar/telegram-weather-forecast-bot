package com.manzar.telegramweatherbot.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.manzar.telegramweatherbot.keyboard.SettingsKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.Language;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.service.factory.UserRequestFactory;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LanguageEnteredHandlerTest {

  @Mock
  private MessageSendingService messageSendingService;
  @Mock
  private UserSessionService userSessionService;
  @Mock
  private SettingsKeyboardBuilder settingsKeyboardBuilder;
  @InjectMocks
  private LanguageEnteredHandler languageEnteredHandler;


  @ParameterizedTest
  @MethodSource("provideUserRequestsAndResults")
  void isApplicableWhenSessionStateEqualsWaitingForLanguage(UserRequest userRequest,
      boolean expected) {
    assertEquals(expected, languageEnteredHandler.isApplicable(userRequest));
  }

  @Test
  void handleSendsChosenLanguageEqualsCurrentIfGivenLanguageEqualsCurrent() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_LANGUAGE, Language.EN.getEmojiValue());

    languageEnteredHandler.handle(userRequest);

    verify(messageSendingService, times(1)).sendMessage(userRequest.getUserSession(),
        "chosen.language.equals.current");
  }

  @Test
  void handleSendsChosenLanguageNotSupportedIfGivenLanguageIsNotSupported() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.WAITING_FOR_LANGUAGE);

    languageEnteredHandler.handle(userRequest);

    verify(messageSendingService, times(1)).sendMessage(userRequest.getUserSession(),
        "chosen.language.not.supported");
  }

  @Test
  void handleSendsLanguageUpdatedAndBuildsSettingKeyboardIfGivenLanguageIsNotEqualCurrentAndSupported() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_LANGUAGE, "🇺🇦");

    languageEnteredHandler.handle(userRequest);

    verify(messageSendingService, times(1)).sendMessage(userRequest.getUserSession(),
        "language.updated", settingsKeyboardBuilder.build());
  }

  @Test
  void handleChangesLanguageAndSessionStateIfLanguageUpdated() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_LANGUAGE, "🇺🇦");
    UserSession userSession = userRequest.getUserSession();

    languageEnteredHandler.handle(userRequest);

    verify(userSessionService, times(1)).editUserSession(userSession);
    assertEquals(ConversationState.CONVERSATION_STARTED, userSession.getConversationState());
    assertEquals(Language.UA, userSession.getLanguage());
  }


  private static Stream<Arguments> provideUserRequestsAndResults() {
    UserRequest requestWithoutText = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.WAITING_FOR_LANGUAGE);
    requestWithoutText.getUpdate().getMessage().setText(null);
    return Stream.of(
        Arguments.of(
            UserRequestFactory.createRequestWithStateAndTestMessage(
                ConversationState.WAITING_FOR_LANGUAGE),
            true),
        Arguments.of(UserRequestFactory.createRequestWithStateAndTestMessage(
            ConversationState.CONVERSATION_STARTED), false),
        Arguments.of(requestWithoutText, false));
  }
}

