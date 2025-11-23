package org.cn.personalapi.infrastructure.port;

import org.cn.personalapi.domain.ai.dto.PersonalColorResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AIModelPort {
    PersonalColorResponse ask(MultipartFile multipartFile);
}
