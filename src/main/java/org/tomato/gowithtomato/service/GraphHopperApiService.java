package org.tomato.gowithtomato.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.dto.GeoResponse;
import org.tomato.gowithtomato.dto.PointDTO;
import org.tomato.gowithtomato.exception.GraphHopperApiException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


@Slf4j
public class GraphHopperApiService {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String KEY = System.getenv("api_gh_key");
    private static final String HTTP_REQUEST_GET_COORd_POINT_BY_NAME = "https://graphhopper.com/api/1/geocode?q=%s&locale=ru&key=%s";
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

        HttpResponse<String> response = httpSender(String.format(HTTP_REQUEST_GET_COORd_POINT_BY_NAME, name, KEY));
        if (response.statusCode() == 200) {
            GeoResponse geoResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});
            return geoResponse.getPointDTOList();
        }
        log.error("response code : {}", response.statusCode());
        throw new GraphHopperApiException();
    }

}
