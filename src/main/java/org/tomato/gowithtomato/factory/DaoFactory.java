package org.tomato.gowithtomato.factory;

import org.tomato.gowithtomato.dao.daoInterface.*;
import org.tomato.gowithtomato.dao.daoInterface.m2m.RouteAndPointsDao;
import org.tomato.gowithtomato.dao.daoInterface.m2m.TripParticipantsDAO;
import org.tomato.gowithtomato.dao.impl.*;

public class DaoFactory {
    public static PointDAO getPointDAO() {
        return PointDAOImpl.getInstance();
    }

    public static ReviewDAO getReviewDAO() {
        return ReviewDAOImpl.getInstance();
    }

    public static RouteDAO getRouteDAO() {
        return RouteDAOImpl.getInstance();
    }

    public static TripDAO getTripDAO() {
        return TripDAOImpl.getInstance();
    }

    public static UserDAO getUserDAO() {
        return UserDAOImpl.getInstance();
    }

    public static RouteAndPointsDao getRouteAndPointsDAO() {
        return RouteAndPointsDaoImpl.getInstance();
    }

    public static TripParticipantsDAO getTripParticipantsDAO() {
        return TripParticipantsDAOImpl.getInstance();
    }
}
