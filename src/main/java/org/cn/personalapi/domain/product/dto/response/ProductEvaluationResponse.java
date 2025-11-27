package org.cn.personalapi.domain.product.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductEvaluationResponse(
    @JsonProperty("suitable")
    boolean suitable,

    @JsonProperty("score")
    int score,

    @JsonProperty("reason")
    String reason,

    @JsonProperty("colorAnalysis")
    String colorAnalysis,

    @JsonProperty("recommendation")
    String recommendation
) {
}
