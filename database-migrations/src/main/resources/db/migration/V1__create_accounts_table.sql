CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE accounts(
    account_id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    account_number   VARCHAR(8) NOT NULL,
    sort_code        VARCHAR(6) NOT NULL,
    account_name     VARCHAR(34) NOT NULL,
    name_updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    account_balance  DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    account_status   VARCHAR(50) NOT NULL CHECK(account_status IN ('ACTIVE','SUSPENDED','CLOSED')),
    created_date     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for fast lookups
CREATE UNIQUE INDEX IF NOT EXISTS idx_accounts_account_number
    ON accounts(account_number);

CREATE INDEX IF NOT EXISTS idx_accounts_sort_code_account_number
    ON accounts(sort_code, account_number);

CREATE INDEX IF NOT EXISTS idx_accounts_status
    ON accounts(account_status);
