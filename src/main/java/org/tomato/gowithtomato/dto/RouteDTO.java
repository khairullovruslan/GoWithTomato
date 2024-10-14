package org.tomato.gowithtomato.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteDTO {
    private Long id;

    private UserDTO owner;

    @JsonProperty("start")
    private PointDTO start;
    @JsonProperty("others")
    private List<PointDTO> others;

    @JsonProperty("finish")
    private PointDTO finish;

    @JsonProperty("distance")
    private double distance;
}
