package com.mixfiscal.produtos_api.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ErrorApiResponse {
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private Integer status;
    private String error;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String path;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors;
}