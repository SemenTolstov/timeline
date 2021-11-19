package com.timeline.controller;

import com.timeline.dto.MessageDto;
import com.timeline.dto.UserDto;
import com.timeline.exception.AccessErrorException;
import com.timeline.exception.MessageNotFoundException;
import com.timeline.exception.UserNotFoundException;
import com.timeline.model.Message;
import com.timeline.model.User;
import com.timeline.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.data.domain.PageRequest.of;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
class MessageControllerTest {

    @InjectMocks
    MessageController messageController;

    @Mock
    private MessageService messageService;

    @Test
    public void testGetAllMessages() {
        Pageable paging = PageRequest.of(0, 10, Sort.by("dateOfAddingAsUtc").ascending());
        Message message1 = new Message(createMessageDto());
        Message message2 = new Message(createMessageDto());
        Page<Message> messages = new PageImpl<>(Arrays.asList(message1, message2));

        Mockito.when(messageService.findAllMessages(paging)).thenReturn(messages);

        ResponseEntity<Page<Message>> result = messageController.getAllMessages(paging);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(messages.getContent(), Objects.requireNonNull(result.getBody()).getContent());
    }

    @Test
    public void testMessagesByUser() throws UserNotFoundException {
        Message message1 = new Message(createMessageDto());
        Message message2 = new Message(createMessageDto());
        List<Message> messages = Arrays.asList(message1, message2);
        User user = new User(createUserDto());

        Mockito.when(messageService.getAllMessagesByUser(user.getUuid())).thenReturn(messages);

        ResponseEntity<Object> result = messageController.getMessagesByUser(user.getUuid());

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(messages, result.getBody());
    }

    @Test
    public void testAddMessage() throws UserNotFoundException {
        MessageDto messageDto = createMessageDto();
        Message message = new Message(messageDto);
        message.setId(1L);
        User user = new User(createUserDto());

        Mockito.when(messageService.addMessage(user.getUuid(), messageDto)).thenReturn(message.getId());

        ResponseEntity<Object> result = messageController.addMessage(user.getUuid(), messageDto);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(message.getId(), result.getBody());
    }

    @Test
    public void testDeleteMessage() throws UserNotFoundException, MessageNotFoundException, AccessErrorException {
        Long messageId = 1L;
        User user = new User(createUserDto());

        Mockito.doNothing().when(messageService).deleteMessage(user.getUuid(), messageId);

        ResponseEntity<String> result = messageController.deleteMessage(user.getUuid(),messageId);

        assertEquals(200, result.getStatusCodeValue());
        Mockito.verify(messageService, Mockito.times(1)).deleteMessage(user.getUuid(), messageId);


    }

    @Test
    public void testUpdateMessage() throws UserNotFoundException, MessageNotFoundException, AccessErrorException {
        MessageDto messageDto = createMessageDto();
        Message message = new Message(messageDto);
        message.setId(1L);
        User user = new User(createUserDto());

        Mockito.when(messageService.updateMessage(user.getUuid(), message.getId(), messageDto)).thenReturn(message.getId());

        ResponseEntity<Object> result = messageController.updateMessage(user.getUuid(), message.getId(), messageDto);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(message.getId(), result.getBody());
    }

    private MessageDto createMessageDto() {
        MessageDto messageDto = new MessageDto();
        messageDto.setHead("testhead");
        messageDto.setText("Some text for test");
        return messageDto;
    }

    private UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setLogin("testlogin");
        userDto.setPassword("TestPassword123");
        return userDto;
    }

    private User createUser(UserDto userDto) {
        User user = new User(userDto);
        user.setPassword(userDto.getPassword());

        assertNotNull(user);
        assertNotNull(user.getUuid());
        assertEquals(user.getLogin(), userDto.getLogin());
        return user;
    }


}