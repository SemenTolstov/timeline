package com.timeline.dto;

import lombok.Data;

import java.util.List;


@Data
public class MessageListDto {

    private List<MessageDto> messages;
    private int page;
    private int size;
    private SortDirection sortDirection;

}
