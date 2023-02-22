CREATE TABLE IF NOT EXISTS user_session
(
    telegram_id        BIGINT,
    city               VARCHAR(255),
    conversation_state VARCHAR(255) NOT NULL,
    CONSTRAINT user_session_PK PRIMARY KEY (telegram_id)
);