package com.timeline.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Data
public class MessageDTO {

    @NotBlank(message = "Please fill the head of message")
    private String head;

    @NotBlank(message = "Please fill the text message")
    @Size(min = 10, max = 1000)
    private String text;
}
