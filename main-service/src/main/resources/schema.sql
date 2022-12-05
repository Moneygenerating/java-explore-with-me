CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(50) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS category (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT pk_category_request PRIMARY KEY(id),
    CONSTRAINT CATEGORY_UQ UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS messages (
   id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   message VARCHAR(5000) NOT NULL,
   user_id BIGINT,
   created_on TIMESTAMP,
   CONSTRAINT pk_message PRIMARY KEY(id),
   CONSTRAINT MESSAGE_INITIATOR FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS conversations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    creator_id BIGINT,
    received_id BIGINT,
    created_on TIMESTAMP,
    CONSTRAINT pk_conversations PRIMARY KEY(id),
    CONSTRAINT CONVERSATION_INITIATOR FOREIGN KEY (creator_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT CONVERSATION_RECEIVE FOREIGN KEY (received_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS conversations_messages (
    conversation_id BIGINT NOT NULL,
    messages_id BIGINT NOT NULL,
    CONSTRAINT CONVERSATION_MESSAGES FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE,
    CONSTRAINT MESSAGE_BY_CONVERSATION FOREIGN KEY (messages_id) REFERENCES messages(id) ON DELETE CASCADE,
    CONSTRAINT PK_CONV_MESSAGES primary key (conversation_id, messages_id)
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation VARCHAR(1000) NOT NULL,
    category_id BIGINT NOT NULL,
    created_on TIMESTAMP NOT NULL,
    description VARCHAR(5000),
    event_date TIMESTAMP NOT NULL,
    initiator_id BIGINT,
    lat FLOAT4,
    lon FLOAT4,
    paid BOOLEAN NOT NULL,
    participant_limit INTEGER NOT NULL,
    confirmed_requests INTEGER DEFAULT 0,
    published_on TIMESTAMP,
    request_moderation BOOLEAN,
    state VARCHAR(50),
    title VARCHAR(150),
    CONSTRAINT pk_events_request PRIMARY KEY(id),
    CONSTRAINT EVENT_REQ_CATEGORY FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
    CONSTRAINT EVENT_REQ_INITIATOR FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN NOT NULL,
    title VARCHAR(150) NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS compilations_events (
    compilation_id BIGINT NOT NULL,
    events_id BIGINT NOT NULL,

    CONSTRAINT EVENT_REQ_COMPILATION FOREIGN KEY (events_id) REFERENCES events(id) ON DELETE CASCADE,
    CONSTRAINT COMPILATIONS_REQ_EVENT FOREIGN KEY (compilation_id) REFERENCES compilations(id) ON DELETE CASCADE,
    CONSTRAINT PK_COMP_EV primary key (compilation_id, events_id)
);

CREATE TABLE IF NOT EXISTS participation_request (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT NOT NULL,
    created TIMESTAMP,
    requester_id BIGINT NOT NULL,
    status VARCHAR(50),

    CONSTRAINT pk_participation PRIMARY KEY(id),
    CONSTRAINT REQUEST_PART_EVENT FOREIGN KEY(event_id) REFERENCES events(id) ON DELETE CASCADE,
    CONSTRAINT REQUEST_USER_ID FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE
);

