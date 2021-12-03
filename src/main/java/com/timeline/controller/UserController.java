package com.timeline.controller;

import com.timeline.dto.UserDto;
import com.timeline.exception.UserAlreadyExistException;
import com.timeline.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("timeline")
@Tag(name = "Пользователи", description = "Регистрация пользователей")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    @Operation(summary = "Регистрация пользователя", description = "Позволяет зарегестрировать пользователя")
    public String registrationUserAccount(@Valid @RequestBody UserDto userDTO) throws UserAlreadyExistException {

        return "Ваш уникальный номер: " + userService.addUser(userDTO);
    }
}
