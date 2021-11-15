package com.timeline.dto;

import com.timeline.constraint.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@Schema(description = "Пользователь")
public class UserDto {

    @NotBlank(message = "Please fill the login")
    @Size(message = "Login must be more than 6 and no more than 10 characters in length",
            min = 6, max = 10)
    @Schema(description = "Логин пользователя")
    private String login;

    @ValidPassword
    @NotBlank(message = "Please fill the password")
    @Schema(description = "Пароль пользователя", minLength = 5, maxLength = 10)
    private String password;
}
