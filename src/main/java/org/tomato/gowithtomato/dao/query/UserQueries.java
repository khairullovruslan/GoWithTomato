package org.tomato.gowithtomato.dao.query;

/**
 * Класс, содержащий SQL-запросы для работы с пользователями - UserDAO.
 */
public class UserQueries {

    //language=sql
    /**
     * Запрос для сохранения нового пользователя
     */
    public static final String SAVE_SQL = """
            INSERT INTO users (login, password, email, phone_number)
            VALUES (?, ?, ?, ?)
            """;

    //language=sql
    /**
     * Запрос для поиска пользователя по логину
     */
    public static final String FIND_BY_LOGIN_SQL = """
            SELECT id, login, email, phone_number
            FROM users
            WHERE login = ?
            """;

    //language=sql
    /**
     * Запрос для поиска пользователя по идентификатору
     */
    public static final String FIND_BY_ID_SQL = """
            SELECT id, login, email, phone_number
            FROM users
            WHERE id = ?
            """;
}
