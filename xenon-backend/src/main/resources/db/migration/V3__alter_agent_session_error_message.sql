-- Alter agent_sessions table to change error_message from VARCHAR(255) to TEXT
ALTER TABLE agent_sessions ALTER COLUMN error_message TYPE TEXT;
