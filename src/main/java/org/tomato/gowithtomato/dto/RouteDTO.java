package org.tomato.gowithtomato.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteDTO {
    @JsonProperty("start")
    private PointDTO start;
    @JsonProperty("others")
    private List<PointDTO> others;

    @JsonProperty("finish")
    private PointDTO finish;
}
