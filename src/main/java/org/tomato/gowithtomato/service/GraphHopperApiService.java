package org.tomato.gowithtomato.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.dto.GeoResponse;
import org.tomato.gowithtomato.dto.GraphInfoResponse;
import org.tomato.gowithtomato.dto.PointDTO;
import org.tomato.gowithtomato.dto.RouteDTO;
import org.tomato.gowithtomato.exception.common.GraphHopperApiException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;


@Slf4j
public class GraphHopperApiService {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final String KEY = System.getenv("api_gh_key");
    private final String KEY = "696a439e-9a37-42f6-8b5a-61315f8af609";
    private static final String HTTP_REQUEST_GET_COORD_POINT_BY_NAME = "https://graphhopper.com/api/1/geocode?q=%s&locale=ru&key=%s";
    private static final String HTTP_REQUEST_GET_ROUTE_INFO_BY_COORDS = "https://graphhopper.com/api/1/route?profile=car&locale=ru&points_encoded=false&key=%s&";
    private static final GraphHopperApiService INSTANCE = new GraphHopperApiService();
    private GraphHopperApiService(){}

    public static GraphHopperApiService getInstance() {
        return INSTANCE;
    }


    private HttpResponse<String> httpSender(String uri) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(uri))
                .GET()
                .build();
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public List<PointDTO> getPointsByName(String name) throws URISyntaxException, IOException, InterruptedException {

        HttpResponse<String> response = httpSender(String.format(HTTP_REQUEST_GET_COORD_POINT_BY_NAME, name, KEY));
        if (response.statusCode() == 200) {
            GeoResponse geoResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});
            return geoResponse.pointDTOList();
        }
        log.error("response code : {}", response.statusCode());
        throw new GraphHopperApiException();
    }

    public HashMap<String, String>  getInfo(RouteDTO routeDTO) throws URISyntaxException, IOException, InterruptedException {
        String url = String.format(HTTP_REQUEST_GET_ROUTE_INFO_BY_COORDS, KEY);

        StringJoiner stringJoiner = convertRouteToUrl(routeDTO);
        HttpResponse<String> response = httpSender(url + stringJoiner);
        GraphInfoResponse graphInfoResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});
        return new HashMap<>(Map.of("time", convertMillisToHHMMSS(graphInfoResponse.getPaths().getFirst().getTime()),
                "distance", String.valueOf(Math.round(graphInfoResponse.getPaths().getFirst().getDistance() / 1000))));


    }

    private static StringJoiner convertRouteToUrl(RouteDTO routeDTO) {
        StringJoiner stringJoiner = new StringJoiner("&");
        String pointTemplate = "point=%s,%s";
        stringJoiner.add(String.format(pointTemplate, routeDTO.getStart().getCoordPoint().getLat(),
                routeDTO.getStart().getCoordPoint().getLng()));
        for (PointDTO point: routeDTO.getOthers()){
            stringJoiner.add(String.format(pointTemplate, point.getCoordPoint().getLat(), point.getCoordPoint().getLng()));
        }
        stringJoiner.add(String.format(pointTemplate, routeDTO.getFinish().getCoordPoint().getLat(),
                routeDTO.getFinish().getCoordPoint().getLng()));
        return stringJoiner;
    }

    public static String convertMillisToHHMMSS(long millis) {
        long seconds = millis / 1000; 
        long hours = seconds / 3600;
        seconds %= 3600; 
        long minutes = seconds / 60; 
        seconds %= 60; 

        return String.format("%02d:%02d:%02d", hours, minutes, seconds); 
    }

}
