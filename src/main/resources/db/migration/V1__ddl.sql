CREATE TABLE IF NOT EXISTS user_session
(
    telegram_id        BIGINT,
    city               VARCHAR(255),
    conversation_state VARCHAR(255) NOT NULL,
    language           VARCHAR(255),
    metric_system      VARCHAR(255),
    CONSTRAINT user_session_PK PRIMARY KEY (telegram_id)
);

CREATE TABLE IF NOT EXISTS notifications
(
    id                BIGSERIAL,
    telegram_id       BIGINT,
    chat_id           BIGINT,
    notification_type VARCHAR(255),
    notification_time TIME WITHOUT TIME ZONE,
    CONSTRAINT notifications_PK PRIMARY KEY (id),
    CONSTRAINT notifications_user_session_FK FOREIGN KEY (telegram_id) REFERENCES user_session (telegram_id)

);