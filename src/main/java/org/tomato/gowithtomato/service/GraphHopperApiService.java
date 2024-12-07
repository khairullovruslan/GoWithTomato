package org.tomato.gowithtomato.service;

import org.tomato.gowithtomato.dto.PointDTO;
import org.tomato.gowithtomato.dto.route.RouteDTO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;


public interface GraphHopperApiService {


    List<PointDTO> getPointsByName(String name) throws URISyntaxException, IOException, InterruptedException;

    HashMap<String, String> getInfo(RouteDTO routeDTO) throws URISyntaxException, IOException, InterruptedException;


}
