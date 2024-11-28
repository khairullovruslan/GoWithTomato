package org.tomato.gowithtomato.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoResponse(
        @JsonProperty("hits") List<PointDTO> pointDTOList
) {
}