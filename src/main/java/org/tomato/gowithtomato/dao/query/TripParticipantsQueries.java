package org.tomato.gowithtomato.dao.query;

public class TripParticipantsQueries {

    public static final String FIND_USERS_BY_TRIP_ID_SQL = """
        SELECT * FROM trip_participants
        WHERE trip_id = ?
    """;

    public static final String SAVE_SQL = """
        INSERT INTO trip_participants (trip_id, user_id)
        VALUES (?, ?)
    """;
}
