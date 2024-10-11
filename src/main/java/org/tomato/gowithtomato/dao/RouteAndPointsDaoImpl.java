package org.tomato.gowithtomato.dao;

import org.tomato.gowithtomato.dao.daoInterface.RouteAndPointsDao;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.exception.DaoException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RouteAndPointsDaoImpl implements RouteAndPointsDao {
    private final static RouteAndPointsDaoImpl INSTANCE = new RouteAndPointsDaoImpl();
    private final PointDAOImpl pointDAO;
    private RouteAndPointsDaoImpl(){
        pointDAO = PointDAOImpl.getInstance();
    }

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


}
