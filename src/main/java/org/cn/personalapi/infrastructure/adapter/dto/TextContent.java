package org.cn.personalapi.infrastructure.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextContent implements ContentPart {
    @Builder.Default
    private String type = "text";
    private String text;

    public TextContent(String text) {
        this.type = "text";
        this.text = text;
    }
}
