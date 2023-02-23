package com.manzar.telegramweatherbot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents user session.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "telegramId")
@Getter
@Setter
@Entity
@Table(name = "user_session")
public class UserSession {

  @Id
  @Column(name = "telegram_id")
  private Long telegramId;
  @Column(name = "city")
  private String city;
  @Column(name = "conversation_state")
  private ConversationState conversationState;

}
