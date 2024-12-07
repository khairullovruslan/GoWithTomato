package org.tomato.gowithtomato.dao.query.m2m;


public class TripParticipantsQueries {

    //language=sql
    public static final String FIND_USERS_BY_TRIP_ID_SQL = """
            SELECT *
            FROM users
                     LEFT JOIN  trip_participants a ON id = a.user_id
            WHERE trip_id = ?;
            """;

    //language=sql
    public static final String SAVE_SQL = """
            INSERT INTO trip_participants (trip_id, user_id)
            VALUES (?, ?)
            """;
}
