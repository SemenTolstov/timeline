package com.timeline.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@Schema(description = "Сущность сообщения")
public class MessageDto {

    @NotBlank(message = "Please fill the head of message")
    @Size(message = "Head must be more than 3 and no more than 10 characters in length", min = 3, max = 10)
    @Schema(description = "Заголовок сообщения")
    private String head;

    @NotBlank(message = "Please fill the text message")
    @Size(message = "Text must be more than 10 characters in length", min = 10, max = 1000)
    @Schema(description = "Текст сообщения")
    private String text;
}
