package org.tomato.gowithtomato.dao.query.m2m;

/**
 * Класс, содержащий SQL-запросы для работы с участниками поездок TripParticipantsDAO
 */
public class TripParticipantsQueries {

    //language=sql
    /**
     * Запрос для поиска пользователей по идентификатору поездки
     */
    public static final String FIND_USERS_BY_TRIP_ID_SQL = """
            SELECT *
            FROM users
                     LEFT JOIN  trip_participants a ON id = a.user_id
            WHERE trip_id = ?;
            """;

    //language=sql
    /**
     * Запрос для сохранения участника поездки
     */
    public static final String SAVE_SQL = """
            INSERT INTO trip_participants (trip_id, user_id)
            VALUES (?, ?)
            """;
}
