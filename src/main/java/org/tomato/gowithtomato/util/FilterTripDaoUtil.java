package org.tomato.gowithtomato.util;

import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.dto.FilterQueriesDTO;
import org.tomato.gowithtomato.entity.TripStatus;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.tomato.gowithtomato.dao.query.TripQueries.*;


@Slf4j
public class FilterTripDaoUtil {
    private static final FilterTripDaoUtil INSTANCE = new FilterTripDaoUtil();

    private FilterTripDaoUtil() {
    }

    public static FilterTripDaoUtil getInstance() {
        return INSTANCE;
    }

    public FilterQueriesDTO getQueryByFilter(final Map<String, String> filter) {
        StringBuilder query = new StringBuilder(SQL_FILTER);
        StringBuilder queryForGetCountTotalPage = new StringBuilder(COUNT_SQL);
        HashMap<String, Integer> keyIdxForPreparedStatement = new HashMap<>();

        List<String> conditions = buildConditions(filter, keyIdxForPreparedStatement);
        if (!conditions.isEmpty()) {
            String fil = " WHERE %s".formatted(String.join(" AND ", conditions));
            if (filter.containsKey("organizer")) {
                fil = "JOIN users u ON t.user_id = u.id %s".formatted(fil);
            }
            if (filter.containsKey("owner_tickets")) {
                fil = "JOIN trip_participants tp on tp.trip_id = t.id %s".formatted(fil);
            }
            query.append(fil);
            queryForGetCountTotalPage.append(fil);
        }

        log.error("filter  page + " + filter.get("page"));
        if (filter.containsKey("page")) {
            query.append(String.format(" limit %d offset %d", LIMIT,
                    LIMIT * (Integer.parseInt(filter.get("page")) - 1)));
        }


        return FilterQueriesDTO
                .builder()
                .countFilterSql(queryForGetCountTotalPage.toString())
                .findByFilterSql(query.toString())
                .keyIdxForPreparedStatement(keyIdxForPreparedStatement)
                .build();
    }

    private static List<String> buildConditions(final Map<String, String> filter,
                                                HashMap<String, Integer> keyIdxForPreparedStatement) {
        List<String> conditions = new ArrayList<>();
        int count = 1;
        for (String key : filter.keySet()) {
            switch (key) {
                case "from" -> conditions.add("p1.name = ?");
                case "to" -> conditions.add("p2.name = ?");
                case "count" -> conditions.add("t.available_seats >= ?");
                case "organizer" -> conditions.add("u.login = ?");
                case "status" -> conditions.add("status = ?");
                case "date" -> conditions.add("t.trip_date_time >= ?");
                case "owner_tickets" -> conditions.add("tp.user_id = ?");

            }
            if (!key.equals("page")) keyIdxForPreparedStatement.put(key, count++);
        }
        return conditions;
    }

    public void insertValueFromFilterIntoPreparedStatement(final Map<String, String> filter,
                                                           final PreparedStatement preparedStatement,
                                                           final FilterQueriesDTO filterQueriesDTO) throws SQLException {
        HashMap<String, Integer> idx = filterQueriesDTO.keyIdxForPreparedStatement();
        for (String key : filter.keySet()) {
            if (key.equals("page")) continue;
            switch (key) {
                case "from", "to", "organizer" -> preparedStatement.setString(idx.get(key), filter.get(key));
                case "count" -> preparedStatement.setInt(idx.get(key), Integer.parseInt(filter.get(key)));
                case "date" -> preparedStatement.setTimestamp(idx.get(key),
                        Timestamp.valueOf(LocalDateTime.parse(filter.get(key))));
                case "status" -> preparedStatement.setObject(idx.get(key),
                        TripStatus.valueOf(filter.get(key)), Types.OTHER);
                case "owner_tickets" -> preparedStatement.setLong(idx.get(key), Long.parseLong(filter.get(key)));
            }
        }
    }
}
