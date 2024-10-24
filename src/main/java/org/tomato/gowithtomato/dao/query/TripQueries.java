package org.tomato.gowithtomato.dao.query;

public class TripQueries {

    public static final Integer LIMIT = 5;
    public static final String COUNT_SQL = """
        SELECT COUNT(*) FROM trip t
        JOIN route r ON t.route_id = r.id
        JOIN Point p1 ON r.start_point_id = p1.id
        JOIN Point p2 ON r.finish_point_id = p2.id""";

    public static final String SAVE_SQL = """
        INSERT INTO trip(user_id, route_id, trip_date_time, available_seats, price, status)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

    public static final String FIND_ALL_SQL = """
        SELECT * FROM trip
    """;

    public static final String ADD_NEW_MEMBER_SQL = """
        UPDATE trip
        SET available_seats = available_seats - 1
        WHERE id = ? AND available_seats > 0
    """;

    public static final String FIND_BY_ID_SQL = """
        SELECT * FROM trip
        WHERE id = ?
    """;

    public static final String SQL_FILTER = """
        SELECT *
        FROM trip t
        JOIN route r ON t.route_id = r.id
        JOIN Point p1 ON r.start_point_id = p1.id
        JOIN Point p2 ON r.finish_point_id = p2.id
    """;
}
