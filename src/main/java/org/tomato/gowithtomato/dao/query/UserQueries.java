package org.tomato.gowithtomato.dao.query;

public class UserQueries {

    public static final String SAVE_SQL = """
        INSERT INTO users (login, password, email, phone_number)
        VALUES (?, ?, ?, ?)
    """;

    public static final String FIND_BY_LOGIN = """
        SELECT * FROM users
        WHERE login = ?
    """;

    public static final String FIND_BY_ID_SQL = """
        SELECT * FROM users
        WHERE id = ?
    """;
}
