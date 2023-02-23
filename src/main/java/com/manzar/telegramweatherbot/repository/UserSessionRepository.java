package com.manzar.telegramweatherbot.repository;

import com.manzar.telegramweatherbot.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

}
