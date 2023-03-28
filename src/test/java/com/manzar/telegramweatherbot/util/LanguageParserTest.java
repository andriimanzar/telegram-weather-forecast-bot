package com.manzar.telegramweatherbot.util;

import com.manzar.telegramweatherbot.model.Language;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LanguageParserTest {

  @ParameterizedTest
  @MethodSource("provideEmojiAndResults")
  void fromEmojiValueReturnsOptionalWithLanguage(String emoji, Optional<Language> expected) {
    Assertions.assertEquals(expected, LanguageParser.fromEmojiValue(emoji));
  }

  private static Stream<Arguments> provideEmojiAndResults() {
    return Stream.of(
        Arguments.of(null, Optional.empty()),
        Arguments.of(" ", Optional.empty()),
        Arguments.of("test", Optional.empty()),
        Arguments.of("🇺🇦", Optional.of(Language.UA)),
        Arguments.of("🇬🇧", Optional.of(Language.EN)));
  }

}