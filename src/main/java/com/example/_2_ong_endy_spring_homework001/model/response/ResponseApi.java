package com.example._2_ong_endy_spring_homework001.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseApi<T> {
    private Boolean success;
    private String message;
    private HttpStatus status;
    private T payload;
    private LocalDateTime timestamp;
}

