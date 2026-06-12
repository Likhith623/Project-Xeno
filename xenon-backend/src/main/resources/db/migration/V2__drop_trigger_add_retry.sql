ALTER TABLE channel_callbacks ADD COLUMN IF NOT EXISTS retry_count INT DEFAULT 0;

DROP TRIGGER IF EXISTS trg_process_callback ON channel_callbacks;
DROP FUNCTION IF EXISTS fn_apply_callback;
