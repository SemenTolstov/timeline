package com.timeline.model;

import com.timeline.dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Setter
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID uuid;

    private String login;

    private String password;

    public User(UserDto userDTO) {
        uuid = UUID.randomUUID();
        login = userDTO.getLogin().toLowerCase();
        password = userDTO.getPassword();
    }
}
