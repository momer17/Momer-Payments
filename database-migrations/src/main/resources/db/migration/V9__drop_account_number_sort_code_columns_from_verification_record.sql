DROP INDEX IF EXISTS idx_verification_record_sender_account_number;

ALTER TABLE verification_record
    DROP COLUMN IF EXISTS sender_account_number,
    DROP COLUMN IF EXISTS sender_sort_code,
    DROP COLUMN IF EXISTS receiver_account_number,
    DROP COLUMN IF EXISTS receiver_sort_code;