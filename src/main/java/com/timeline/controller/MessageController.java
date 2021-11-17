package com.timeline.controller;

import com.timeline.dto.MessageDto;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("timeline/messages")
@Tag(name = "Контроллер новостей(сообщений)", description = "Позволяет управлять сообщениями")
public class MessageController {

    private static final String DESCRIPTOR = "Уникальный номер пользователя. Посмтреть можно только в БД. " +
            "Выдается при регистрации.";

    @Autowired
    private AppService appService;

    @GetMapping
    @Operation(summary = "Получение всех сообщений",
            description = "Позволяет отобразить сообщения в зависимости от входных параметров. " +
                    "(Количество отображаемых сообщений, порядок сортировки)")
    public ResponseEntity<Page<Message>> showAllMessages(@Parameter(description = "Параметры отображения сообщений от пользователя") Pageable pageable) {
        Page<Message> messagePage = appService.findAllMessages(pageable);

        return new ResponseEntity<>(messagePage, HttpStatus.OK);
    }

    @GetMapping("/users/{uuid}")
    @Operation(summary = "Получение сообщений",
            description = "Получение сообщений конкретного пользователя")
    public ResponseEntity<String> getMessagesByUser(@PathVariable @Parameter(description = DESCRIPTOR) UUID uuid) throws UserNotFoundException {

        try {
            return new ResponseEntity(appService.getAllMessagesByUser(uuid), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/users/{uuid}")
    @Operation(summary = "Добавить сообщение",
            description = "Позволяет пользователю добавлять сообщения")
    public ResponseEntity<String> addMessage(@PathVariable @Parameter(description = DESCRIPTOR) String uuid,
                                             @Valid @RequestBody MessageDto messageDto) throws UserNotFoundException {
        try {
            UUID uuidToFind = UUID.fromString(uuid);
            appService.addMessage(uuidToFind, messageDto);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/users/{uuid}/messages/{messageId}")
    @Operation(summary = "Удаление сообщения",
            description = "Позволяет пользователю удалить сообщение")
    public ResponseEntity<String> deleteMessage(@PathVariable @Parameter(description = DESCRIPTOR) String uuid,
                                                @PathVariable @Parameter(description = "Идентификациионый номер сообщения") Long messageId) throws UserNotFoundException,
            MessageNotFoundException, AccessErrorException {

        try {
            UUID uuidToFind = UUID.fromString(uuid);
            appService.deleteMessage(uuidToFind, messageId);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/users/{uuid}/messages/{messageId}")
    @Operation(summary = "Изменить сообщение",
            description = "Позволяет пользователю изменить существующее сообщение")
    public ResponseEntity<String> updateMessage(@PathVariable @Parameter(description = DESCRIPTOR) String uuid,
                                                @PathVariable @Parameter(description = "Идентификациионый номер сообщения") Long messageId,
                                                @Valid @RequestBody MessageDto messageDTO) throws UserNotFoundException,
            MessageNotFoundException, AccessErrorException {

        try {
            UUID uuidToFind = UUID.fromString(uuid);
            appService.updateMessage(uuidToFind, messageId, messageDTO);
            return new ResponseEntity<String>(HttpStatus.OK);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
