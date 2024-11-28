package org.tomato.gowithtomato.dto;

import lombok.Builder;

import java.util.HashMap;

@Builder
public record FilterQueriesDTO(String countFilterSql,
                               String findByFilterSql, HashMap<String, Integer> keyIdxForPreparedStatement) {
}
