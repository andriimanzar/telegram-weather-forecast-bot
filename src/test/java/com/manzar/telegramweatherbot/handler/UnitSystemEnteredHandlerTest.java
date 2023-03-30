package com.manzar.telegramweatherbot.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.prominence.openweathermap.api.enums.UnitSystem;
import com.manzar.telegramweatherbot.keyboard.SettingsKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.LocalizationService;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.service.factory.UserRequestFactory;
import com.manzar.telegramweatherbot.util.UnitSystemParser;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UnitSystemEnteredHandlerTest {

  @Mock
  private MessageSendingService messageSendingService;
  @Mock
  private UserSessionService userSessionService;
  @Mock
  private LocalizationService localizationService;
  @Mock
  private SettingsKeyboardBuilder settingsKeyboardBuilder;

  @InjectMocks
  private UnitSystemEnteredHandler unitSystemEnteredHandler;

  private static MockedStatic<UnitSystemParser> unitSystemParserStaticMock;

  @BeforeAll
  static void setUp() {
    unitSystemParserStaticMock = mockStatic(UnitSystemParser.class,
        Mockito.CALLS_REAL_METHODS);

  }

  @AfterAll
  static void closeResources() {
    unitSystemParserStaticMock.close();
  }


  @ParameterizedTest
  @MethodSource("provideUserRequestsAndResults")
  void isApplicableWhenSessionStateEqualsWaitingForUnitSystem(UserRequest userRequest,
      boolean expected) {
    assertEquals(expected, unitSystemEnteredHandler.isApplicable(userRequest));
  }

  @Test
  void handleSendsChosenUnitSystemNotSupportedMessageIfGivenUnitSystemWasNotFound() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.WAITING_FOR_UNIT_SYSTEM);
    UserSession userSession = userRequest.getUserSession();

    unitSystemParserStaticMock.when(() ->
            UnitSystemParser.parse(any(LocalizationService.class), any(UserSession.class), anyString()))
        .thenReturn(Optional.empty());

    unitSystemEnteredHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userSession, "chosen.unit.system.not.supported");
  }

  @Test
  void handleSendsChosenUnitSystemEqualsCurrentMessageIfGivenUnitSystemEqualsCurrent() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.WAITING_FOR_UNIT_SYSTEM);
    UserSession userSession = userRequest.getUserSession();

    unitSystemParserStaticMock.when(() ->
            UnitSystemParser.parse(any(LocalizationService.class), any(UserSession.class), anyString()))
        .thenReturn(Optional.of(userSession.getUnitSystem()));

    unitSystemEnteredHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userSession, "chosen.unit.system.equals.current");
  }

  @Test
  void handleSendsUnitSystemUpdatedMessageAndBuildsSettingsKeyboardIfGivenUnitSystemNotEqualsCurrent() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.WAITING_FOR_UNIT_SYSTEM);
    UserSession userSession = userRequest.getUserSession();

    unitSystemParserStaticMock.when(() ->
            UnitSystemParser.parse(any(LocalizationService.class), any(UserSession.class), anyString()))
        .thenReturn(Optional.of(UnitSystem.IMPERIAL));

    unitSystemEnteredHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userSession, "unit.system.updated", settingsKeyboardBuilder.build());
  }


  @Test
  void handleChangesSessionToConversationStartedIfGivenUnitSystemNotEqualsCurrent() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.WAITING_FOR_UNIT_SYSTEM);
    UserSession userSession = userRequest.getUserSession();

    unitSystemParserStaticMock.when(() ->
            UnitSystemParser.parse(any(LocalizationService.class), any(UserSession.class), anyString()))
        .thenReturn(Optional.of(UnitSystem.IMPERIAL));

    unitSystemEnteredHandler.handle(userRequest);

    assertEquals(ConversationState.CONVERSATION_STARTED,
        userSession.getConversationState());
    verify(userSessionService, times(1)).editUserSession(userSession);
  }

  private static Stream<Arguments> provideUserRequestsAndResults() {
    UserRequest requestWithoutText = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.WAITING_FOR_UNIT_SYSTEM);
    requestWithoutText.getUpdate().getMessage().setText(null);
    return Stream.of(
        Arguments.of(
            UserRequestFactory.createRequestWithStateAndTestMessage(
                ConversationState.WAITING_FOR_UNIT_SYSTEM),
            true),
        Arguments.of(UserRequestFactory.createRequestWithStateAndTestMessage(
            ConversationState.CONVERSATION_STARTED), false),
        Arguments.of(requestWithoutText, false));
  }
}