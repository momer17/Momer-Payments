CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE  IF NOT EXISTS verification_record (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sender_account_number   VARCHAR(255) NOT NULL,
    sender_sort_code        VARCHAR(255) NOT NULL,
    receiver_account_number VARCHAR(255) NOT NULL,
    receiver_sort_code      VARCHAR(255) NOT NULL,
    requested_name          VARCHAR(255) NOT NULL,
    actual_name             VARCHAR(255) NOT NULL,
    match_result            VARCHAR(50) NOT NULL CHECK (match_result IN ('EXACT_MATCH', 'CLOSE_MATCH', 'NO_MATCH')),
    confidence              DOUBLE PRECISION CHECK (confidence >= 0 AND confidence <= 1),
    verified_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_verification_record_created_at
    ON verification_record (verified_at);