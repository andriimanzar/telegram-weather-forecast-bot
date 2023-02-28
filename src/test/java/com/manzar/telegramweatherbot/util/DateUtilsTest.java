package com.manzar.telegramweatherbot.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


class DateUtilsTest {

  @ParameterizedTest
  @MethodSource("provideDates")
  void isValidReturnsTrueIfDateIsValid(String date, boolean expected) {
    assertEquals(expected, DateUtils.isValid(date));
  }

  @ParameterizedTest
  @MethodSource("provideStringDatesAndExpectedLocalDates")
  void parseReturnsCorrectExpectedDate(String date, LocalDate parsedDate) {
    assertEquals(parsedDate, DateUtils.parse(date));
  }

  private static Stream<Arguments> provideDates() {
    return Stream.of(
        Arguments.of("12/02", true),
        Arguments.of("1/03", true),
        Arguments.of("15/00", false),
        Arguments.of("9/13", false),
        Arguments.of("15/2", true),
        Arguments.of("32/03", false),
        Arguments.of("50/12", false),
        Arguments.of("00/13", false),
        Arguments.of("5/2", true));
  }

  private static Stream<Arguments> provideStringDatesAndExpectedLocalDates() {
    return Stream.of(
        Arguments.of("12/10", LocalDate.of(LocalDate.now().getYear(), 10, 12)),
        Arguments.of("8/5", LocalDate.of(LocalDate.now().getYear(), 5, 8)),
        Arguments.of("30/1", LocalDate.of(LocalDate.now().getYear(), 1, 30)),
        Arguments.of("25/8", LocalDate.of(LocalDate.now().getYear(), 8, 25)),
        Arguments.of("1/5", LocalDate.of(LocalDate.now().getYear(), 5, 1))
    );
  }

}