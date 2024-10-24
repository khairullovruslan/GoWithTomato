package org.tomato.gowithtomato.dao.query;

public class RouteQueries {
    public final static String SAVE_SQL =
            """
                    INSERT INTO route(start_point_id, finish_point_id, distance, user_id) values (?, ?, ?, ?)
                    """;


    private final static String FIND_TEMPLATE = """
            SELECT r.id AS route_id,
                r.start_point_id,
                r.finish_point_id,
                r.distance,
                u.id AS user_id,
                u.login as user_login,
                u.email as user_email,
                u.phone_number as user_phone_number,
                p1.id AS start_id,
                p1.lat AS start_lat,
                p1.lng AS start_lng,
                p1.name AS start_name,
                p1.country AS start_country,
                p1.osm_value AS start_osm_value,
                p1.state AS start_state,
 
                p2.id AS finish_id,
                p2.lat AS finish_lat,
                p2.lng AS finish_lng,
                p2.name AS finish_name,
                p2.country AS finish_country,
                p2.osm_value AS finish_osm_value,
                p2.state AS finish_state
             FROM route r
                 JOIN users u ON r.user_id = u.id
                 JOIN point p1 ON r.start_point_id = p1.id
                 JOIN point p2 ON r.finish_point_id = p2.id
            """;
    public final static String FIND_BY_USER_ID_SQL =
            FIND_TEMPLATE +
             """
             WHERE user_id = ?
             """;
    public static final String FIND_BY_ID_SQL =
            FIND_TEMPLATE +
            """
            WHERE r.id = ?
            """;

}
