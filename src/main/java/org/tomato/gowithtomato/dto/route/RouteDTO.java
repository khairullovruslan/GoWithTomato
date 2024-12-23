package org.tomato.gowithtomato.dto.route;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.tomato.gowithtomato.dto.PointDTO;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteDTO {
    private Long id;

    @JsonProperty("start")
    private PointDTO start;
    @JsonProperty("others")
    private List<PointDTO> others;

    @JsonProperty("finish")
    private PointDTO finish;

    @JsonProperty("distance")
    private double distance;
}
