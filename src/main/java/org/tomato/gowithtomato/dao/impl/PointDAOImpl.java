package org.tomato.gowithtomato.dao.impl;

import org.tomato.gowithtomato.dao.daoInterface.PointDAO;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.exception.db.DaoException;
import org.tomato.gowithtomato.mapper.PointMapper;

import java.sql.*;
import java.util.Optional;

import static org.tomato.gowithtomato.dao.query.PointQueries.*;

public class PointDAOImpl extends PointDAO {
    private static final PointDAOImpl INSTANCE = new PointDAOImpl();

    private PointDAOImpl() {
        this.mapper = PointMapper.getInstance();
    }

    public static PointDAOImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Point save(Point entity) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setDouble(1, entity.getLat());
            statement.setDouble(2, entity.getLng());
            statement.setString(3, entity.getName());
            statement.setString(4, entity.getCountry());
            statement.setString(5, entity.getState());
            statement.setString(6, entity.getOsmValue());

            int affectedRows = statement.executeUpdate();
            System.out.println("affr " + affectedRows);
            if (affectedRows == 0) {
                throw new DaoException("Не удалось сохранить точку.");
            }



            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setId(keys.getLong(1));
                }
            }
            System.out.println("ent "  + entity);
            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
            handleSQLException(e);
            throw new DaoException("Ошибка при сохранении точки.", e);
        }
    }

    @Override
    public Optional<Point> findByLatLng(Double lat, Double lng) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_COORDINATE_SQL)) {
            statement.setDouble(1, lat);
            statement.setDouble(2, lng);
            ResultSet result = statement.executeQuery();
            return convertResultSetToOptional(result);
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске точки по координатам", e);
        }
    }

    @Override
    public Optional<Point> findById(Long id) {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            return convertResultSetToOptional(result);
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске точки по ID", e);
        }
    }

}
