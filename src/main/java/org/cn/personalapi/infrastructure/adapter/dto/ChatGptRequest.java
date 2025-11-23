package org.cn.personalapi.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatGptRequest {

    private String model;

    private List<Message> messages;

    private Double temperature;

    private Integer max_tokens;

    private Double frequency_penalty;

    @JsonProperty("response_format")
    private Map<String, String> responseFormat;
}
