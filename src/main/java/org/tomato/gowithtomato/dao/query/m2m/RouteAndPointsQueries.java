package org.tomato.gowithtomato.dao.query.m2m;

/**
 * Класс, содержащий SQL-запросы для работы с маршрутами и точками - RouteAndPointsDao
 */
public class RouteAndPointsQueries {

    //language=sql
    /**
     * Запрос для поиска точек по идентификатору маршрута
     */
    public static final String FIND_BY_ID_SQL =
            """
                    SELECT
                        p.id AS id,
                        p.lat AS lat,
                        p.lng AS lng,
                        p.name AS title,
                        p.country AS country,
                        p.state AS state,
                        p.osm_value AS osm_value
                    FROM
                        route_intermediate_points rip
                    JOIN
                        point p ON rip.point_id = p.id
                    WHERE
                        rip.route_id = ?
                    ORDER BY
                        rip.sequence;
                    """;

    //language=sql
    /**
     * Запрос для вставки точки в промежуточную таблицу маршрутов
     */
    public static final String ROUTE_INTERMEDIATE_INSERT_SQL =
            """
                    INSERT INTO route_intermediate_points(route_id, point_id, sequence) VALUES (?, ?, ?)
                    """;
}
