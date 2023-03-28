package com.manzar.telegramweatherbot.util;

import com.github.prominence.openweathermap.api.enums.UnitSystem;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.LocalizationService;
import java.util.Optional;

/**
 * Provides methods for parsing a unit system from a user input text.
 */
public class UnitSystemParser {

  /**
   * Parses a unit system from a given text string using the provided localization service and user
   * session.
   *
   * @param localizationService the service used to localize the button labels
   * @param userSession         current user session
   * @param text                the text to parse the unit system from
   * @return an optional containing the parsed unit system if one was found, or an empty optional if
   *     the text did not match a supported unit system
   */
  public static Optional<UnitSystem> parse(LocalizationService localizationService,
      UserSession userSession, String text) {
    if (localizationService.localizedButtonLabelEqualsGivenText(userSession, text,
        "metric.unit.system")) {
      return Optional.of(UnitSystem.METRIC);
    } else if (localizationService.localizedButtonLabelEqualsGivenText(userSession, text,
        "imperial.unit.system")) {
      return Optional.of(UnitSystem.IMPERIAL);
    } else {
      return Optional.empty();
    }
  }

}
