package org.tomato.gowithtomato.factory;

import org.tomato.gowithtomato.service.*;
import org.tomato.gowithtomato.service.impl.*;

public class ServiceFactory {

    public static AuthService getAuthService() {
        return AuthServiceImpl.getInstance();
    }

    public static GraphHopperApiService getGraphHopperApiService() {
        return GraphHopperApiServiceImpl.getInstance();
    }

    public static ReviewService getReviewService() {
        return ReviewServiceImpl.getInstance();
    }

    public static RouteService getRouteService() {
        return RouteServiceImpl.getInstance();
    }

    public static TripParticipantsService getTripParticipantsService() {
        return TripParticipantsServiceImpl.getInstance();
    }

    public static TripService getTripService() {
        return TripServiceImpl.getInstance();
    }

    public static UserService getUserService() {
        return UserServiceImpl.getInstance();
    }

    public static CloudinaryService getCloudinaryService(){
        return CloudinaryServiceImpl.getInstance();
    }


}
