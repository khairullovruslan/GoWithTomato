package org.tomato.gowithtomato.dto.route;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


@Builder
public record NewRouteResponse(

        @JsonProperty("routeInfo")
        RouteDTO routeDTO,

        @JsonProperty("infoType")
        String type) {
}
