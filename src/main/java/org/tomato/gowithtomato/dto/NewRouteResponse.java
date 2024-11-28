package org.tomato.gowithtomato.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewRouteResponse {

    @JsonProperty("routeInfo")
    private RouteDTO routeDTO;

    @JsonProperty("infoType")
    private String type;


}
