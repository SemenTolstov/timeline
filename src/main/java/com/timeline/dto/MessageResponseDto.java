package com.timeline.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.timeline.model.Message;
import com.timeline.model.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponseDto {

    private Long id;

    private User user;

    private String head;

    private String text;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime dateOfAddingAsUtc;

    public MessageResponseDto(Message message) {
        id = message.getId();
        user = message.getUser();
        head = message.getHead();
        text = message.getText();
        dateOfAddingAsUtc = message.getDateOfAddingAsUtc();
    }
}
