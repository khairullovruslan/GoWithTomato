package org.tomato.gowithtomato.dao.query;

/**
 * Класс, содержащий SQL-запросы для работы с точками  - PointDAO
 */
public class PointQueries {

    //language=sql
    /**
     * Запрос для сохранения точки с возможностью обновления в случае конфликта
     */
    public static final String SAVE_SQL =
            """
                    INSERT INTO point(lat, lng, name, country, state, osm_value)
                    VALUES (?, ?, ?, ?, ?, ?)
                    ON CONFLICT (lat, lng)
                    DO UPDATE SET
                        name = excluded.name,
                        country = excluded.country,
                        state = excluded.state,
                        osm_value = excluded.osm_value
                    RETURNING id
                    """;

    //language=sql
    /**
     * Запрос для поиска точки по координатам (широта и долгота)
     */
    public static final String FIND_BY_COORDINATE_SQL =
            """
                    SELECT id, lat, lng, name, country, state, osm_value
                    FROM point
                    WHERE lat = ? AND lng = ?
                    """;

    //language=sql
    /**
     * Запрос для поиска точки по идентификатору
     */
    public static final String FIND_BY_ID_SQL =
            """
                    SELECT id, lat, lng, name, country, state, osm_value
                    FROM point
                    WHERE id = ?
                    """;
}
