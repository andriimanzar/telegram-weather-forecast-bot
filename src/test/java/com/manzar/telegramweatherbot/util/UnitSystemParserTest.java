package com.manzar.telegramweatherbot.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.prominence.openweathermap.api.enums.UnitSystem;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.LocalizationService;
import com.manzar.telegramweatherbot.service.factory.UserSessionFactory;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;

@ExtendWith(MockitoExtension.class)
class UnitSystemParserTest {

  private static LocalizationService localizationService;

  @BeforeAll
  static void beforeAll() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("message");
    messageSource.setDefaultEncoding("UTF-8");
    localizationService = new LocalizationService(messageSource);

  }

  @ParameterizedTest
  @MethodSource("provideTextAndResults")
  void parseReturnsExpectedOptionalWithUnitSystem(String text, Optional<UnitSystem> expected) {
    UserSession userSession = UserSessionFactory.createTestUserSession();
    assertEquals(expected, UnitSystemParser.parse(localizationService, userSession, text));
  }

  private static Stream<Arguments> provideTextAndResults() {
    return Stream.of(
        Arguments.of(" ", Optional.empty()),
        Arguments.of("test", Optional.empty()),
        Arguments.of("📏 Metric", Optional.of(UnitSystem.METRIC)),
        Arguments.of("📏 Imperial", Optional.of(UnitSystem.IMPERIAL)));
  }

}