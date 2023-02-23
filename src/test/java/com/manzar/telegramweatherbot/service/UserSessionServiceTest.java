package com.manzar.telegramweatherbot.service;

import static org.junit.jupiter.api.Assertions.*;

import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.repository.UserSessionRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserSessionServiceTest {

  @Mock
  private UserSessionRepository userSessionRepository;

  @InjectMocks
  private UserSessionService userSessionService;

  @Test
  void getUserSessionReturnsUserSessionIfUserSessionExists() {
    UserSession userSession = UserSessionFactory.createTestUserSession();
    Mockito.when(userSessionRepository.findById(1L)).thenReturn(Optional.of(userSession));

    assertEquals(userSession, userSessionService.getUserSession(1L).get());
  }

  @Test
  void createUserSessionCallsRepositorySave() {
    UserSession userSession = UserSessionFactory.createTestUserSession();
    Mockito.when(userSessionRepository.save(ArgumentMatchers.any(UserSession.class)))
        .thenReturn(userSession);

    userSessionService.createUserSession(1L);

    Mockito.verify(userSessionRepository, Mockito.times(1)).save(userSession);
  }

  @Test
  void createUserSessionDoesNothingIfUserSessionExists() {
    UserSession userSession = UserSessionFactory.createTestUserSession();
    Mockito.when(userSessionRepository.findById(1L)).thenReturn(Optional.of(userSession));

    userSessionService.createUserSession(1L);

    Mockito.verify(userSessionRepository, Mockito.times(0)).save(userSession);
  }

  @Test
  void editUserSessionCallsRepositoryMethodSave() {
    UserSession userSession = UserSessionFactory.createTestUserSession();
    UserSession updatedUserSession = UserSessionFactory.createTestUserSession();
    updatedUserSession.setConversationState(ConversationState.WAITING_FOR_CITY);
    Mockito.when(userSessionRepository.save(ArgumentMatchers.any(UserSession.class)))
        .thenReturn(userSession);

    userSessionService.editUserSession(updatedUserSession);

    Mockito.verify(userSessionRepository, Mockito.times(1)).save(userSession);
  }
}

