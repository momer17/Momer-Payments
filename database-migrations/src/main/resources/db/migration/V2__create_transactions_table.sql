CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE transactions (
    transaction_id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sender_account_id           UUID NOT NULL,
    receiver_account_id         UUID NOT NULL,
    payment_description         VARCHAR(255),
    payment_reference_number    VARCHAR(255) NOT NULL,
    idempotency_key             VARCHAR(255) NOT NULL,
    currency                    VARCHAR(255) NOT NULL CHECK(currency IN('SOS','GBP','USD')),
    amount                      DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    transaction_status          VARCHAR(50) NOT NULL CHECK(transaction_status IN('PENDING','PENDING_VERIFICATION','PROCESSING','SETTLED','FAILED', 'CANCELLED')),
    created_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key constraints
    CONSTRAINT fk_payee_owner_account
        FOREIGN KEY (sender_account_id) REFERENCES accounts(account_id),

    CONSTRAINT fk_payee_receiver_account
        FOREIGN KEY (receiver_account_id) REFERENCES accounts(account_id)
);


CREATE UNIQUE INDEX IF NOT EXISTS idx_transactions_payment_reference
    ON transactions(payment_reference_number);

CREATE INDEX IF NOT EXISTS idx_transactions_sender_account_id
    ON transactions(sender_account_id);

CREATE INDEX IF NOT EXISTS idx_transactions_receiver_account_id
    ON transactions(receiver_account_id);

CREATE INDEX IF NOT EXISTS idx_transactions_status
    ON transactions(transaction_status);

CREATE INDEX IF NOT EXISTS idx_transactions_sender_status
    ON transactions(sender_account_id, transaction_status);