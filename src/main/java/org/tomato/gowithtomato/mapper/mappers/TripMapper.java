package org.tomato.gowithtomato.mapper.mappers;

import org.tomato.gowithtomato.dao.daoInterface.RouteDAO;
import org.tomato.gowithtomato.dao.daoInterface.UserDAO;
import org.tomato.gowithtomato.dao.impl.RouteDAOImpl;
import org.tomato.gowithtomato.dao.impl.UserDAOImpl;
import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.entity.TripStatus;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.db.DaoException;
import org.tomato.gowithtomato.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TripMapper implements RowMapper<Trip> {
    private final static TripMapper INSTANCE = new TripMapper();
    private final UserDAO userDAO;
    private final RouteDAO routeDAO;

    private TripMapper() {
        userDAO = UserDAOImpl.getInstance();
        routeDAO = RouteDAOImpl.getInstance();

    }

    public static TripMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Trip mapRow(ResultSet result) throws SQLException {
        Optional<User> owner = userDAO.findById(result.getLong("user_id"));
        Optional<Route> route = routeDAO.findById(result.getLong("route_id"));
        if (owner.isEmpty() || route.isEmpty()) {
            throw new DaoException("Route or User are null");
        }

        return Trip.builder()
                .id(result.getLong("id"))
                .price(result.getBigDecimal("price"))
                .availableSeats(result.getInt("available_seats"))
                .tripDateTime(result.getTimestamp("trip_date_time").toLocalDateTime())
                .status(TripStatus.valueOf(result.getString("status")))
                .owner(owner.get())
                .route(route.get())
                .build();
    }
}
