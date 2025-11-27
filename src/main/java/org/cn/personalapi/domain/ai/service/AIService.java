package org.cn.personalapi.domain.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cn.personalapi.domain.ai.dto.PersonalColorResponse;
import org.cn.personalapi.domain.ai.prompt.Prompt;
import org.cn.personalapi.domain.report.dto.response.PersonalColorReportDto;
import org.cn.personalapi.infrastructure.port.AIModelPort;
import org.cn.personalapi.infrastructure.port.AiTextProcessorPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AIService {

    private final AiTextProcessorPort aiTextProcessorPort;
    private final AIModelPort aiModelPort;
    private final ObjectMapper objectMapper;

    public PersonalColorResponse extractPersonalColor(MultipartFile imageFile) {
        return aiModelPort.ask(imageFile);
    }

    public PersonalColorReportDto getPersonalColorReportDto(PersonalColorResponse personalColorResponse) {
        Prompt prompt = Prompt.create(personalColorResponse);
        String gptResponse = aiTextProcessorPort.ask(prompt.getContent());
        log.info("GPT Response: {}", gptResponse);

        try {
            String cleanedResponse = gptResponse.strip();
            if (cleanedResponse.startsWith("```json")) {
                cleanedResponse = cleanedResponse.substring(7);
            } else if (cleanedResponse.startsWith("```")) {
                cleanedResponse = cleanedResponse.substring(3);
            }
            if (cleanedResponse.endsWith("```")) {
                cleanedResponse = cleanedResponse.substring(0, cleanedResponse.length() - 3);
            }
            cleanedResponse = cleanedResponse.strip();

            return objectMapper.readValue(cleanedResponse, PersonalColorReportDto.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse GPT response to PersonalColorResultDto", e);
            throw new RuntimeException("GPT 응답을 파싱하는데 실패했습니다.", e);
        }
    }

}
