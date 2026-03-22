ALTER TABLE transactions DROP CONSTRAINT transactions_transaction_status_check;

ALTER TABLE transactions ADD CONSTRAINT transactions_transaction_status_check
CHECK (transaction_status IN ('INITIATED', 'PENDING_VERIFICATION', 'PROCESSING', 'SETTLED', 'FAILED'));