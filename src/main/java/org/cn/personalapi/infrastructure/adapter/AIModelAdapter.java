package org.cn.personalapi.infrastructure.adapter;

import org.cn.personalapi.domain.ai.dto.PersonalColorResponse;
import org.cn.personalapi.infrastructure.port.AIModelPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AIModelAdapter implements AIModelPort {

    private final WebClient fastApiWebClient;

    public AIModelAdapter(@Qualifier("fastApiWebClient") WebClient fastApiWebClient) {
        this.fastApiWebClient = fastApiWebClient;
    }

    @Override
    public PersonalColorResponse ask(MultipartFile multipartFile) {
        return fastApiWebClient.post()
            .uri("/analyze")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData("file", multipartFile.getResource()))
            .retrieve()
            .onStatus(HttpStatusCode::isError, ClientResponse::createException)
            .bodyToMono(PersonalColorResponse.class)
            .block();
    }
}
