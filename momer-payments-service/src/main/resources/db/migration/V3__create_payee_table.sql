CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE payees (
    payee_id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    owner_account_id    UUID NOT NULL,
    receiver_account_id UUID NOT NULL,
    display_name        VARCHAR(255) NOT NULL,
    verified_name       VARCHAR(255) NOT NULL,
    match_result        VARCHAR(50) NOT NULL CHECK (match_result IN ('EXACT_MATCH', 'CLOSE_MATCH', 'NO_MATCH')),
    confidence_score    DECIMAL(5,4),
    verified_at         TIMESTAMP NOT NULL,

    -- Foreign key constraints

    -- Unique constraint: one payee entry per sender-receiver pair
    CONSTRAINT uk_payee_owner_receiver
        UNIQUE (owner_account_id, receiver_account_id)
);

-- Index for fast lookups of a user's payees
CREATE INDEX idx_payee_owner_account_id
    ON payee (owner_account_id);

-- Composite index
CREATE INDEX idx_payee_owner_receiver
    ON payee (owner_account_id, receiver_account_id);

