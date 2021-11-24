package com.timeline.model;

import com.timeline.dto.MessageDto;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;


@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "msg_seq")
    @SequenceGenerator(name = "msg_seq", sequenceName = "msg_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "head", nullable = false, length = 10)
    @Size(message = "Head must be more than 3 and no more than 10 characters in length", min = 3, max = 10)
    private String head;

    @Column(name = "text", nullable = false, length = 1000)
    @Size(message = "Text must be more than 10 characters in length", min = 10, max = 1000)
    private String text;

    @Column(name = "date_of_adding_as_utc", nullable = false)
    private LocalDateTime dateOfAddingAsUtc;

    public Message(MessageDto messageDTO) {
        head = messageDTO.getHead();
        text = messageDTO.getText();
        dateOfAddingAsUtc = LocalDateTime.now(ZoneOffset.UTC);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Message message = (Message) o;
        return id != null && Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
