CREATE TABLE saga_event (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    saga_id       UUID NOT NULL REFERENCES saga_instance(id) ON DELETE CASCADE,
    event_type    VARCHAR(100) NOT NULL,
    event_payload JSONB NOT NULL,
    occurred_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_saga_event_saga_id
    ON saga_event (saga_id);

CREATE INDEX idx_saga_event_occurred_at
    ON saga_event (occurred_at);

CREATE INDEX idx_saga_event_event_type
    ON saga_event (event_type);