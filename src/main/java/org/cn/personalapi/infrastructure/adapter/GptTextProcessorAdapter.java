package org.cn.personalapi.infrastructure.adapter;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.cn.personalapi.infrastructure.adapter.dto.ChatGptRequest;
import org.cn.personalapi.infrastructure.adapter.dto.ChatGptResponse;
import org.cn.personalapi.infrastructure.adapter.dto.Message;
import org.cn.personalapi.infrastructure.port.AiTextProcessorPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class GptTextProcessorAdapter implements AiTextProcessorPort {

    private final WebClient webClient;

    public GptTextProcessorAdapter(@Qualifier("externalWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String ask(String userMessage) {

        Message message = createUserMessage(userMessage);

        ChatGptRequest request = createRequest(message);

        long startTime = System.nanoTime();

        ChatGptResponse chatGptResponse = webClient.post()
            .uri("/chat/completions")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .bodyToMono(ChatGptResponse.class)
            .block();

        long endTime = System.nanoTime();
        long durationMillis = (endTime - startTime) / 1_000_000;
        log.info("GPT API 호출 시간: {}ms", durationMillis);

        if (chatGptResponse == null || chatGptResponse.getChoices().isEmpty()) {
            return "GPT 응답을 가져오지 못했습니다.";
        }

        Object content = chatGptResponse.getChoices().get(0).getMessage().getContent();
        return content instanceof String ? (String) content : content.toString();
    }

    private static ChatGptRequest createRequest(Message message) {
        return ChatGptRequest.builder()
            .model("gpt-4o-mini")
            .messages(List.of(message))
            .temperature(0.0)
            .max_tokens(600)
            .frequency_penalty(0.0) // temperature: 모델이 응답을 생성할 때의 창의성 또는 무작위성을 조절합니다.
            .build();
    }

    private static Message createUserMessage(String userMessage) {
        return Message.builder()
            .role("user")
            .content(userMessage)
            .build();
    }

    private Message createSystemMessage(String systemMessage) {
        return Message.builder()
            .role("system")
            .content(systemMessage)
            .build();
    }
}
