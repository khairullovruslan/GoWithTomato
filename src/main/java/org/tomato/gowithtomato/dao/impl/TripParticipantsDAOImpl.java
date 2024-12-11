package org.tomato.gowithtomato.dao.impl;

import org.tomato.gowithtomato.dao.daoInterface.m2m.TripParticipantsDAO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.db.DaoException;
import org.tomato.gowithtomato.mapper.RowMapper;
import org.tomato.gowithtomato.mapper.UserMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.tomato.gowithtomato.dao.query.TripQueries.ADD_NEW_MEMBER_SQL;
import static org.tomato.gowithtomato.dao.query.m2m.TripParticipantsQueries.FIND_USERS_BY_TRIP_ID_SQL;
import static org.tomato.gowithtomato.dao.query.m2m.TripParticipantsQueries.SAVE_SQL;

public class TripParticipantsDAOImpl extends TripParticipantsDAO {
    private static final TripParticipantsDAOImpl INSTANCE = new TripParticipantsDAOImpl();
    private final RowMapper<User> userRowMapper;

    private TripParticipantsDAOImpl() {
        userRowMapper = UserMapper.getInstance();
    }

    public static TripParticipantsDAOImpl getInstance() {
        return INSTANCE;
    }


    @Override
    public List<User> findUsersByTripId(Long tripId) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USERS_BY_TRIP_ID_SQL)) {
            preparedStatement.setLong(1, tripId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(userRowMapper.mapRow(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске пользователей по tripId");
        }
    }


    @Override
    public void save(Long tripId, Long userId) {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL);
                 PreparedStatement preparedStatementForTrip = connection.prepareStatement(ADD_NEW_MEMBER_SQL)) {
                preparedStatement.setLong(1, tripId);
                preparedStatement.setLong(2, userId);
                preparedStatement.executeUpdate();

                preparedStatementForTrip.setLong(1, tripId);
                if (preparedStatementForTrip.executeUpdate() > 0) {
                    connection.commit();
                } else {
                    throw new DaoException("Невозможно добавить нового участника: недостаточно мест.");
                }
            } catch (Exception e) {
                connection.rollback();
                throw new DaoException("Ошибка при сохранении участника в поездке", e);
            }
        } catch (SQLException e) {
            throw new DaoException("Ошибка при подключении к базе данных: %s".formatted(e.getMessage()), e);
        }
    }
}
