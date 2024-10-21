package org.tomato.gowithtomato.dao.query;


public class PointQueries {

    public static final String SAVE_SQL =
            """
                    INSERT INTO point(lat, lng, name, country, state, osm_value) VALUES (?, ?, ?, ?, ?, ?)
                    ON CONFLICT (lat, lng) DO UPDATE SET name = excluded.name, country = excluded.country, state = excluded.state, osm_value = excluded.osm_value
                    RETURNING id
                    """;

    public static final String FIND_BY_COORDINATE_SQL =
            """
                    SELECT * FROM point WHERE lat = ? AND lng = ?
                    """;

    public static final String FIND_BY_ID_SQL =
            """
                    SELECT * from point where id = ?
                    """;
}
