package org.cn.personalapi.domain.product.dto.request;

import org.cn.personalapi.domain.PersonalColor;
import org.springframework.web.multipart.MultipartFile;

public record ProductEvaluationRequest(
    MultipartFile productImage,
    PersonalColor personalColor
) {
}
