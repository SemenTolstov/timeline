package com.timeline.controller;

import com.timeline.dto.MessageDto;
import com.timeline.exception.AccessErrorException;
import com.timeline.exception.MessageNotFoundException;
import com.timeline.exception.UserNotFoundException;
import com.timeline.model.Message;
import com.timeline.service.MessageService;
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
@RequestMapping("timeline")
@Tag(name = "Контроллер новостей(сообщений)", description = "Позволяет управлять сообщениями")
public class MessageController {

    private static final String DESCRIPTOR = "Уникальный номер пользователя. Посмтреть можно только в БД. " +
            "Выдается при регистрации.";

    @Autowired
    private MessageService messageService;

    @GetMapping
    @Operation(summary = "Получение всех сообщений",
            description = "Позволяет отобразить сообщения в зависимости от входных параметров. " +
                    "(Количество отображаемых сообщений, порядок сортировки)")
    public ResponseEntity<Page<Message>> getAllMessages(@Parameter(description = "Параметры отображения сообщений от пользователя") Pageable pageable) {
        Page<Message> messagePage = messageService.findAllMessages(pageable);

        return new ResponseEntity<>(messagePage, HttpStatus.OK);
    }

    @GetMapping("/users/{uuid}")
    @Operation(summary = "Получение сообщений",
            description = "Получение сообщений конкретного пользователя")
    public ResponseEntity<Object> getMessagesByUser(@PathVariable @Parameter(description = DESCRIPTOR) UUID uuid) throws UserNotFoundException {

        return new ResponseEntity<>(messageService.getAllMessagesByUser(uuid), HttpStatus.OK);
    }

    @PostMapping("/users/{uuid}")
    @Operation(summary = "Добавить сообщение",
            description = "Позволяет пользователю добавлять сообщения")
    public ResponseEntity<Object> addMessage(@PathVariable @Parameter(description = DESCRIPTOR) UUID uuid,
                                             @Valid @RequestBody MessageDto messageDto) throws UserNotFoundException {
        return new ResponseEntity<>(messageService.addMessage(uuid, messageDto), HttpStatus.OK);
    }

    @DeleteMapping("/users/{uuid}/messages/{messageId}")
    @Operation(summary = "Удаление сообщения",
            description = "Позволяет пользователю удалить сообщение")
    public ResponseEntity<String> deleteMessage(@PathVariable @Parameter(description = DESCRIPTOR) UUID uuid,
                                                @PathVariable @Parameter(description = "Идентификациионый номер сообщения") Long messageId) throws UserNotFoundException,
            MessageNotFoundException, AccessErrorException {

        messageService.deleteMessage(uuid, messageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/users/{uuid}/messages/{messageId}")
    @Operation(summary = "Изменить сообщение",
            description = "Позволяет пользователю изменить существующее сообщение")
    public ResponseEntity<Object> updateMessage(@PathVariable @Parameter(description = DESCRIPTOR) UUID uuid,
                                                @PathVariable @Parameter(description = "Идентификациионый номер сообщения") Long messageId,
                                                @Valid @RequestBody MessageDto messageDTO) throws UserNotFoundException,
            MessageNotFoundException, AccessErrorException {

        return new ResponseEntity<>(messageService.updateMessage(uuid, messageId, messageDTO), HttpStatus.OK);
    }
}
