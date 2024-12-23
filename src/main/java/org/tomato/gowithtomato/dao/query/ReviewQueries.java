package org.tomato.gowithtomato.dao.query;

public class ReviewQueries {

    //language=sql
    public static final String SEARCH_FOR_USER_IN_REVIEWS_SQL = """
            SELECT id, user_id, trip_id, description, rating
            FROM review
            WHERE user_id = ?
              AND trip_id = ?
            """;

    //language=sql
    public static final String SAVE_SQL = """
            INSERT INTO review(user_id, trip_id, description, rating)
            VALUES (?, ?, ?, ?)
            """;

    //language=sql
    public static final String FIND_BY_USER_AND_TRIP_ID_SQL = """
            SELECT id, user_id, trip_id, description, rating
            FROM review
            WHERE user_id = ? AND trip_id = ?
            """;


    //language=sql
    public static final String FIND_BY_TRIP_OWNER_ID_SQL = """
            SELECT DISTINCT
                r.id as id,
                r.user_id as user_id,
                r.trip_id as trip_id,
                r.rating as rating,
                r.description as description
            FROM review r
               JOIN trip t ON r.trip_id = t.id
            WHERE t.user_id = ?
            """;


}
