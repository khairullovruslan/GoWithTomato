package org.tomato.gowithtomato.dao.query;

/**
 * Класс, содержащий SQL-запросы для работы с отзывами  - ReviewDAO
 */
public class ReviewQueries {

    //language=sql
    /**
     * Запрос для поиска отзыва пользователя по идентификатору пользователя и поездки
     */
    public static final String SEARCH_FOR_USER_IN_REVIEWS_SQL = """
            SELECT id, user_id, trip_id, description, rating
            FROM review
            WHERE user_id = ?
              AND trip_id = ?
            """;

    //language=sql
    /**
     * Запрос для сохранения отзыва
     */
    public static final String SAVE_SQL = """
            INSERT INTO review(user_id, trip_id, description, rating)
            VALUES (?, ?, ?, ?)
            """;

    //language=sql
    /**
     * Запрос для поиска отзыва по идентификатору пользователя и поездки
     */
    public static final String FIND_BY_USER_AND_TRIP_ID_SQL = """
            SELECT id, user_id, trip_id, description, rating
            FROM review
            WHERE user_id = ? AND trip_id = ?
            """;
}
