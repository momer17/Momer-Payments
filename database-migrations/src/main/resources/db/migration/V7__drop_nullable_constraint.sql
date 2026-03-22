ALTER TABLE payees
    ALTER COLUMN match_result DROP NOT NULL,
    ALTER COLUMN verified_name DROP NOT NULL,
    ALTER COLUMN verified_at DROP NOT NULL;