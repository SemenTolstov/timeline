package com.timeline.controller;

import com.timeline.dto.MessageDto;
import com.timeline.dto.SortDirection;
import com.timeline.exception.AccessErrorException;
import com.timeline.exception.MessageNotFoundException;
import com.timeline.exception.UserNotFoundException;
import com.timeline.model.Message;
import com.timeline.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;


@RestController
@RequestMapping("timeline")
@Tag(name = "Контроллер новостей(сообщений)", description = "Позволяет управлять сообщениями")
public class MessageController {

    private static final String DESCRIPT = "Уникальный номер пользователя. Посмтреть можно только в БД. " +
            "Выдается при регистрации.";

    @Autowired
    private AppService appService;

    @GetMapping
    @Operation(summary = "Получение всех сообщений",
            description = "Позволяет отобразить сообщения в зависимости от входных параметров. " +
                    "(Количество отображаемых сообщений, порядок сортировки)")
    public ResponseEntity showAllMessages(@RequestParam(defaultValue = "0") @Parameter(description = "Какую страницу надо вернуть") int page,
                                          @RequestParam(defaultValue = "10") @Parameter(description = "Размер возвращаемой страницы") int size,
                                          @RequestParam(defaultValue = "ASC") @Parameter(description = "Вид сортировки") SortDirection sortDirection) {
        Page<Message> messagePage = appService.findAllMessages(page, size, sortDirection);

        return new ResponseEntity(messagePage, HttpStatus.OK);
    }

    @GetMapping("{uuid}")
    @Operation(summary = "Получение сообщений",
            description = "Получение сообщений конкретного пользователя")
    public ResponseEntity getMessagesByUser(@PathVariable @Parameter(description = DESCRIPT) String uuid) throws UserNotFoundException {

        try {
            return new ResponseEntity(appService.getAllMessagesByUser(UUID.fromString(uuid)), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("{uuid}")
    @Operation(summary = "Добавить сообщение",
            description = "Позволяет пользователю добавлять сообщения")
    public ResponseEntity addMessage(@PathVariable @Parameter(description = DESCRIPT) String uuid,
                                     @Valid @RequestBody MessageDto messageDto) throws UserNotFoundException {
        try {
            UUID uuidToFind = UUID.fromString(uuid);
            appService.addMessage(uuidToFind, messageDto);
            return new ResponseEntity(HttpStatus.OK);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("{uuid}/{messageId}")
    @Operation(summary = "Удаление сообщения",
            description = "Позволяет пользователю удалить сообщение")
    public ResponseEntity deleteMessage(@PathVariable @Parameter(description = DESCRIPT) String uuid,
                                        @PathVariable @Parameter(description = "Идентификациионый номер сообщения") Long messageId) throws UserNotFoundException,
            MessageNotFoundException, AccessErrorException {

        try {
            UUID uuidToFind = UUID.fromString(uuid);
            appService.deleteMessage(uuidToFind, messageId);
            return new ResponseEntity(HttpStatus.OK);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("{uuid}/edit/{messageId}")
    @Operation(summary = "Изменить сообщение",
            description = "Позволяет пользователю изменить существующее сообщение")
    public ResponseEntity updateMessage(@PathVariable @Parameter(description = DESCRIPT) String uuid,
                                        @PathVariable @Parameter(description = "Идентификациионый номер сообщения") Long messageId,
                                        @Valid @RequestBody MessageDto messageDTO) throws UserNotFoundException,
            MessageNotFoundException, AccessErrorException {

        try {
            UUID uuidToFind = UUID.fromString(uuid);
            appService.updateMessage(uuidToFind, messageId, messageDTO);
            return new ResponseEntity(HttpStatus.OK);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
