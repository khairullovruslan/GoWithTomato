package org.tomato.gowithtomato.dao.query;

public class UserQueries {

    //language=sql
    public static final String SAVE_SQL = """
            INSERT INTO users (login, password, email, phone_number)
            VALUES (?, ?, ?, ?)
            """;

    //language=sql
    public static final String FIND_BY_LOGIN_SQL = """
            SELECT id, login, email, phone_number, password, photo_url
            FROM users
            WHERE login = ?
            """;

    //language=sql
    public static final String FIND_BY_ID_SQL = """
            SELECT id, login, email, phone_number, password, photo_url
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
            SELECT id, login, email, phone_number, password, photo_url
            FROM users
            WHERE email = ?
            """;

    //language=sql
    public static final String UPDATE_USER_SQL = """
            UPDATE users
            SET login = ?, phone_number = ?, email = ?
            where id = ?
            """;

    //language=sql
    public static final String UPDATE_PHOTO_SQL = """
            UPDATE users
            SET photo_url = ?
            where id = ?
            """;

    //language=sql
    public static final String UPDATE_USER_PASSWORD_SQL = """
            UPDATE users
            SET password = ?
            where id = ?
            """;

    //language=sql
    public static final String DELETE_USER_BY_ID_SQL = """
            DELETE FROM users
            WHERE id = ?
            """;

    //language=sql
    public static final String FIND_BY_TRIP_ID_SQL = """
            SELECT  users.id as id, login, email, phone_number, password, photo_url
            FROM users
            JOIN trip t on t.user_id = users.id
            WHERE t.id = ?
            """;
    //language=sql
    public static final String FIND_BY_ROUTE_ID_SQL = """
           
            SELECT  users.id as id, login, email, phone_number, password, photo_url
            FROM users
            JOIN route r on r.user_id = users.id
            WHERE r.id = ?
            """;

}
