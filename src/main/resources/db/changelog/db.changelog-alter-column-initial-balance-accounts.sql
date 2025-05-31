-- changeset dzmitry-sivachenko:1
ALTER TABLE bank_system.accounts
    ADD COLUMN initial_balance DECIMAL(15, 2) NOT NULL;