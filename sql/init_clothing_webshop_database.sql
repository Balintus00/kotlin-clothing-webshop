CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS articles
(
    id                       text PRIMARY KEY,
    name                     text        NOT NULL,
    price                    integer     NOT NULL,
    brand                    text        NOT NULL,
    color                    integer     NOT NULL,
    shade                    integer     NOT NULL,
    graphical_appearance     integer     NOT NULL,
    index                    integer     NOT NULL,
    garment_group            integer     NOT NULL,
    description              text        NOT NULL,
    creation_date_time       timestamptz NOT NULL,
    recommendation_embedding vector(32)  NOT NULL
);

CREATE TABLE IF NOT EXISTS application_users
(
    id                   text PRIMARY KEY NOT NULL,
    username             text             NOT NULL,
    email                text             NOT NULL,
    password             text             NOT NULL,
    first_name           text             NOT NULL,
    last_name            text             NOT NULL,
    date_of_birth        date             NOT NULL,
    recommendation_index integer
);