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


    //language=sql
    public static final String FIND_BY_TRIP_OWNER_ID_SQL = """
            SELECT
                r.id as id,
                r.user_id as user_id,
                r.trip_id as trip_id,
                r.rating as rating,
                r.description as description
            FROM review r
            LEFT JOIN trip t ON r.trip_id = t.user_id
            WHERE t.user_id = ?
            """;


}
