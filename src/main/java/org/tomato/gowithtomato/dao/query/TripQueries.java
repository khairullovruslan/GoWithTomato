package org.tomato.gowithtomato.dao.query;

public class TripQueries {

    public static final Integer LIMIT = 5;

    //language=sql
    public static final String COUNT_SQL = """
            SELECT COUNT(*) 
            FROM trip t
            
            JOIN route r ON t.route_id = r.id
            JOIN point p1 ON r.start_point_id = p1.id
            JOIN point p2 ON r.finish_point_id = p2.id
            """;

    //language=sql
    public static final String SAVE_SQL = """
            INSERT INTO trip(user_id, route_id, trip_date_time, available_seats, price, status)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    //language=sql
    public static final String FIND_ALL_SQL = """
            SELECT id, user_id, route_id, trip_date_time, available_seats, price, status
            FROM trip
            """;

    //language=sql
    public static final String ADD_NEW_MEMBER_SQL = """
            UPDATE trip
            SET available_seats = available_seats - 1
            WHERE id = ? AND available_seats > 0
            """;

    //language=sql
    public static final String FIND_BY_ID_SQL = """
            SELECT * FROM trip
            WHERE id = ?
            """;

    //language=sql
    public static final String SQL_FILTER = """
            SELECT t.id AS id, t.user_id, t.route_id as route_id, t.trip_date_time, t.available_seats, t.price, t.status, 
                   p1.id AS start_point_id, p1.lat AS start_lat, p1.lng AS start_lng,
                   p2.id AS finish_point_id, p2.lat AS finish_lat, p2.lng AS finish_lng
            FROM trip t
            JOIN route r ON t.route_id = r.id
            JOIN point p1 ON r.start_point_id = p1.id
            JOIN point p2 ON r.finish_point_id = p2.id
 
            """;

    //language=sql
    public static final String CANCEL_TRIP_SQL = """
            UPDATE trip
            SET status = 'cancelled'
            WHERE id = ? 
            """;

    //language=sql
    public static final String UPDATE_STATUS_FOR_COMPLETED_TRIPS_SQL = """
            UPDATE trip
            SET status = 'completed'
            WHERE status <> 'completed'
            AND trip_date_time < NOW() AT TIME ZONE 'Europe/Moscow';
            """;


    //language=sql
    public static final String COUNT_BY_OWNER_ID_SQL = """
            SELECT COUNT(*)
            FROM trip
            WHERE user_id = ?
            """;


    //language=sql
    public static final String CANCEL_TRIP_BY_USER_ID_SQL = """
            UPDATE trip
            SET status = 'cancelled'
            WHERE user_id = ?
            """;
}
