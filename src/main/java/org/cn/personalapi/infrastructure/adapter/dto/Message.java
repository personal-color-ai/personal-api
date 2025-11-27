package org.cn.personalapi.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String role;

    // String(텍스트 메시지) 또는 List<ContentPart>(멀티모달)를 지원
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object content;

}
