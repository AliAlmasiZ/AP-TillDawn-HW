CREATE TABLE IF NOT EXISTS player (
  username  TEXT    PRIMARY KEY,
  password  TEXT    NOT NULL,
  securityAnswer    TEXT    NOT NULL,
  langId    INTEGER NOT NULL,
  id    INTEGER NOT NULL
);
