-- liquibase formatted sql

-- changeset ProteZ63:1
CREATE INDEX name_unique ON student (name)

-- changeset ProteZ63:2
CREATE INDEX color_name ON faculty (color, name);
