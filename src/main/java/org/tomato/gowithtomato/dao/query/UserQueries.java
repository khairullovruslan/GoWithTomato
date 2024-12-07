package org.tomato.gowithtomato.dao.query;

public class UserQueries {

    //language=sql
    public static final String SAVE_SQL = """
            INSERT INTO users (login, password, email, phone_number)
            VALUES (?, ?, ?, ?)
            """;

    //language=sql
    public static final String FIND_BY_LOGIN_SQL = """
            SELECT id, login, email, phone_number, password
            FROM users
            WHERE login = ?
            """;

    //language=sql
    public static final String FIND_BY_ID_SQL = """
            SELECT id, login, email, phone_number, password
            FROM users
            WHERE id = ?
            """;


    //language=sql
    public static final String GET_PASSWORD_BY_LOGIN = """
            SELECT password
            FROM users
            WHERE login = ?
            """;


    //language=sql
    public static final String FIND_BY_EMAIL_SQL = """
            SELECT id, login, email, phone_number, password
            FROM users
            WHERE email = ?
            """;

    //language=sql
    public static final String UPDATE_USER_SQL = """
            UPDATE users
            SET login = ?, phone_number = ?, email = ?
            where id = ?
            """;
}
