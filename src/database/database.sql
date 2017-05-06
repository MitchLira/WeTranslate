CREATE TABLE User(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        email VARCHAR UNIQUE,
        password VARCHAR
);

CREATE TABLE Request(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        field_text VARCHAR,
        translate_from VARCHAR,
        translate_to VARCHAR,
        user_id INTEGER REFERENCES User,
        translation_id INTEGER REFERENCES Translation
);

CREATE TABLE Translation(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        translated_text VARCHAR,
        request_id INTEGER REFERENCES Request,
        translated_user_id INTEGER REFERENCES User /* Utilizador que fez a tradução */
);