package com.timeline.model;

import com.timeline.dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.UUID;


@Entity
@Setter
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usr_seq")
    @SequenceGenerator(name = "usr_seq", sequenceName = "usr_seq", allocationSize = 1)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "login", nullable = false, unique = true)
    @Size
    private String login;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    public User(UserDto userDTO) {
        uuid = UUID.randomUUID();
        login = userDTO.getLogin().toLowerCase();
    }
}
