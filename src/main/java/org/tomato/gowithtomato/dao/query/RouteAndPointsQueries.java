package org.tomato.gowithtomato.dao.query;

public class RouteAndPointsQueries {
    public final static String FIND_BY_ID_SQL =
                    """
                    SELECT
                         p.id AS id,
                         p.lat as lat,
                         p.lng as lng,
                         p.name as title,
                         p.country as country,
                         p.state as state,
                         p.osm_value as osm_value
                     FROM
                         route_intermediate_points rip
                             JOIN
                         point p ON rip.point_id = p.id
                     WHERE
                         rip.route_id = ?
                     ORDER BY
                         rip.sequence;
                    """;
    public final static String ROUTE_INTERMEDIATE_INSERT_SQL =
                    """
                    INSERT INTO route_intermediate_points(route_id, point_id, sequence) VALUES (?, ?, ?)
                    """;
}