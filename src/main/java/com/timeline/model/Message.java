package com.timeline.model;

import com.timeline.dto.MessageDTO;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private String head;

    private String text;

    public Message(MessageDTO messageDTO) {
        head = messageDTO.getHead();
        text = messageDTO.getText();
    }
}
