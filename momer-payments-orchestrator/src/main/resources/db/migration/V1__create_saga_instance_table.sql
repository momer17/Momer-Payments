CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE saga_instance (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    saga_type     VARCHAR(50) NOT NULL CHECK (saga_type IN ('PAYMENT', 'VERIFICATION')),
    current_state VARCHAR(100) NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_saga_instance_saga_type
    ON saga_instance (saga_type);

CREATE INDEX idx_saga_instance_current_state
    ON saga_instance (current_state);

CREATE INDEX idx_saga_instance_created_at
    ON saga_instance (created_at);