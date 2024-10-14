package org.tomato.gowithtomato.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PointDTO {
    @JsonProperty("point")
    private CoordPoint coordPoint;


    @JsonProperty("name")
    private String name;


    @JsonProperty("country")
    private String country;


    @JsonProperty("state")
    private String state;


    @JsonProperty("osm_value")
    private String osmValue;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CoordPoint {
        @JsonProperty("lat")
        private Double lat;
        @JsonProperty("lng")
        private Double lng;

    }

}
