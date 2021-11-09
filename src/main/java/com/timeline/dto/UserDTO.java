package com.timeline.dto;

import com.timeline.constraint.ValidPassword;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Data
public class UserDTO {

    @NotBlank(message = "Please fill the username")
    @Size(min = 6, max = 10)
    private String username;

    @NotBlank(message = "Please fill the password")
    @ValidPassword
    private String password;

    @NotBlank(message = "Please repeat password")
    @ValidPassword
    private String confirmPassword;
}
