package org.tomato.gowithtomato.dao;

import org.tomato.gowithtomato.dao.daoInterface.RouteAndPointsDao;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RouteAndPointsDaoImpl implements RouteAndPointsDao {
    private final static RouteAndPointsDaoImpl INSTANCE = new RouteAndPointsDaoImpl();
    private final PointDAOImpl pointDAO;
    private RouteAndPointsDaoImpl(){
        pointDAO = PointDAOImpl.getInstance();
    }
    private final static String FIND_BY_ID_SQL =
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


    public static RouteAndPointsDaoImpl getInstance() {
        return INSTANCE;
    }

    public void saveAll(Connection connection, List<Point> pointList, Long routeId) throws SQLException {


            String routeIntermediateInsertSQL = "INSERT INTO route_intermediate_points (route_id, point_id, sequence) VALUES (?, ?, ?)";

            try (var routeIntermediateStatement = connection.prepareStatement(routeIntermediateInsertSQL)) {

                int sequence = 1;

                for (Point point : pointList) {
                    Point savePoint;
                    try {
                        savePoint = pointDAO.save(connection, point);
                    }
                    catch (SQLException e){

                        Optional<Point> findPoint = pointDAO.findByLatLng(connection, point.getLat(), point.getLng());
                        if (findPoint.isPresent()){
                            savePoint = findPoint.get();
                        }
                        else{
                            throw new DaoException();
                        }

                    }

                    routeIntermediateStatement.setLong(1, routeId);
                    routeIntermediateStatement.setLong(2, savePoint.getId());
                    routeIntermediateStatement.setInt(3, sequence++);
                    routeIntermediateStatement.addBatch();
                }

                routeIntermediateStatement.executeBatch();
            }
    }
    public List<Point> findByRouteId(Connection connection, Long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            return convertResultSetToList(result);
        }
    }
    private List<Point> convertResultSetToList(ResultSet result) throws SQLException {
        List<Point> points = new ArrayList<>();
        while (result.next()) {
            points.add(Point.builder()
                    .id(result.getLong("id"))
                    .lng(result.getDouble("lng"))
                    .lat(result.getDouble("lat"))
                    .name(result.getString("title"))
                    .state(result.getString("state"))
                    .osmValue(result.getString("osm_value"))
                    .country(result.getString("country"))
                    .build());
        }
        return points;
    }




}
