package com.manzar.telegramweatherbot.service;

import com.github.prominence.openweathermap.api.enums.UnitSystem;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.Language;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.repository.UserSessionRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * UserSession service.
 */
@Service
@RequiredArgsConstructor
public class UserSessionService {

  private final UserSessionRepository userSessionRepository;

  public Optional<UserSession> getUserSession(Long telegramId) {
    return userSessionRepository.findById(telegramId);
  }

  /**
   * Creates new UserSession by given telegramId if session not exists.
   */
  public void createUserSessionIfNotExists(Long telegramId) {
    if (getUserSession(telegramId).isEmpty()) {
      UserSession userSessionToSave = UserSession.builder().telegramId(telegramId)
          .conversationState(ConversationState.CONVERSATION_STARTED).language(Language.EN)
          .unitSystem(UnitSystem.METRIC).build();

      userSessionRepository.save(userSessionToSave);
    }
  }

  public void editUserSession(UserSession updatedSession) {
    userSessionRepository.save(updatedSession);
  }
}
