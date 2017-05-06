CREATE TABLE User(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        username VARCHAR,
        password VARCHAR,
        picture VARCHAR,
        email VARCHAR UNIQUE
);

CREATE TABLE Request(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        field_text VARCHAR,
        user_id INTEGER REFERENCES User,
        translation_id INTEGER REFERENCES Translation
);

CREATE TABLE Translate(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        translated_text VARCHAR,
        request_id INTEGER REFERENCES Request,
        translated_user_id INTEGER REFERENCES User
);