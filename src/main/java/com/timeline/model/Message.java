package com.timeline.model;

import com.timeline.dto.MessageDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private String head;

    private String text;

    @Column(name = "date_of_adding_as_utc")
    private LocalDateTime added;

    public Message(MessageDto messageDTO) {
        head = messageDTO.getHead();
        text = messageDTO.getText();
        added = LocalDateTime.now();
    }
}
