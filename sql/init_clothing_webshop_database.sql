CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS articles
(
    articles_id              SERIAL PRIMARY KEY,
    recommendation_embedding vector(32)
);

INSERT INTO articles (recommendation_embedding)
VALUES ('[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31]'),
       ('[0, -1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11, -12, -13, -14, -15, -16, -17, -18, -19, -20, -21, -22, -23, -24, -25, -26, -27, -28, -29, -30, -31]'),
       ('[100, 100.5, 100, 100.5, 100, 100.5, 100, 100.5, 100, 100.5, 100, 100.5, 100, 100.5, 100, 100.5, 100, 100.5, 100, 100.5, 100, 100.5, 100, 100.5, 100, 100.5, 100, 100.5, 100, 100.5, 100, 100.5]'),
       ('[0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.10, 0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19, 0.20, 0.21, 0.22, 0.23, 0.24, 0.25, 0.26, 0.27, 0.28, 0.29, 0.30, 0.31]'),
       ('[10, -10, 100, -100, 200, -200, 50, -50, 10, -10, 100, -100, 200, -200, 50, -50, 10, -10, 100, -100, 200, -200, 50, -50, 10, -10, 100, -100, 200, -200, 50, -50]'),
       ('[50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50]'),
       ('[-50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50, -50]'),
       ('[30, -30, 80, -80, 15, -15, 0.5, 37.5, 30, -30, 80, -80, 15, -15, 0.5, 37.5, 30, -30, 80, -80, 15, -15, 0.5, 37.5, 30, -30, 80, -80, 15, -15, 0.5, 37.5]'),
       ('[600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600]'),
       ('[-500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500, -500]');