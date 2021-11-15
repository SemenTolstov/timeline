package com.timeline.controller;

import com.timeline.dto.UserDto;
import com.timeline.exception.UserAlreadyExistException;
import com.timeline.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;


@RestController
@RequestMapping("timeline")
@Tag(name = "Пользователи", description = "Регистрация пользователей")
public class UserController {

    @Autowired
    private AppService appService;

    @PostMapping("registration")
    @Operation(summary = "Регистрация пользователя", description = "Позволяет зарегестрировать пользователя")
    public ResponseEntity registrationUserAccount(@Valid @RequestBody UserDto userDTO) throws UserAlreadyExistException {

       UUID yourUuid = appService.addUser(userDTO);

        return new ResponseEntity("Ваш уникальный номер: " + yourUuid, HttpStatus.OK);
    }
}
