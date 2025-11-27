package org.cn.personalapi.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageContent implements ContentPart {
    @Builder.Default
    private String type = "image_url";

    @JsonProperty("image_url")
    private ImageUrl imageUrl;

    public ImageContent(ImageUrl imageUrl) {
        this.type = "image_url";
        this.imageUrl = imageUrl;
    }
}
