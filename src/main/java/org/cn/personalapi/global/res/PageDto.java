package org.cn.personalapi.global.res;

import lombok.Builder;

import java.util.List;

public class PageDto {
    @Builder
    public record Dto (
            List<?> list,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            Boolean isFirst,
            Boolean isLast
    ) {
    }
}
