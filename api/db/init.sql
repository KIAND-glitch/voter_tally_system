BEGIN;
CREATE SCHEMA IF NOT EXISTS app AUTHORIZATION voter_tally_system_owner;
CREATE TABLE IF NOT EXISTS app.test (
 test_value varchar(255)
);
COMMIT;