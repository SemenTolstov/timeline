package com.timeline.service;

import com.timeline.dto.MessageDto;
import com.timeline.dto.UserDto;
import com.timeline.exception.AccessErrorException;
import com.timeline.exception.MessageNotFoundException;
import com.timeline.exception.UserNotFoundException;
import com.timeline.model.Message;
import com.timeline.model.User;
import com.timeline.repo.MessageRepo;
import com.timeline.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest
class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private MessageRepo messageRepo;

    @Test
    public void addMessageTest_whenAddingIsCorrect() throws UserNotFoundException {
        UserDto userDto = createUserDto();
        User user = createUser(userDto);
        MessageDto messageDto = createMessageDto();

        Mockito.when(messageRepo.save(Mockito.any())).thenReturn(new Message());
        Mockito.when(userRepo.findByUuid(user.getUuid())).thenReturn(Optional.of(user));

        messageService.addMessage(user.getUuid(), messageDto);

        Mockito.verify(messageRepo, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void updateMessageTest() throws UserNotFoundException, MessageNotFoundException, AccessErrorException {
        User user = createUser(createUserDto());
        MessageDto messageDto = createMessageDto();
        Message message = new Message();
        message.setHead("testhead2");
        message.setText("Some text for second test");
        message.setUser(user);

        Mockito.when(messageRepo.findById(message.getId())).thenReturn(Optional.of(message));
        Mockito.when(messageRepo.save(message)).thenReturn(message);
        Mockito.when(userRepo.findByUuid(user.getUuid())).thenReturn(Optional.of(user));

        messageService.updateMessage(user.getUuid(), message.getId(), messageDto);

        assertEquals(message.getHead(), messageDto.getHead());
        assertEquals(message.getText(), messageDto.getText());
        Mockito.verify(messageRepo, Mockito.times(1)).save(message);
    }

    @Test
    public void deleteMessageTest() throws UserNotFoundException, MessageNotFoundException, AccessErrorException {
        User user = createUser(createUserDto());
        Message message = new Message();
        message.setUser(user);

        Mockito.when(messageRepo.findById(message.getId())).thenReturn(Optional.of(message));
        Mockito.when(userRepo.findByUuid(user.getUuid())).thenReturn(Optional.of(user));

        messageService.deleteMessage(user.getUuid(), message.getId());

        Mockito.verify(messageRepo, Mockito.times(1)).delete(message);
    }

    @Test
    public void findAllMessagesTest() {
        Pageable paging = PageRequest.of(0, 10, Sort.by("dateOfAddingAsUtc").ascending());

        Mockito.when(messageRepo.findAll(paging)).thenReturn(Page.empty());

        Page<Message> messagePage = messageService.findAllMessages(paging);

        assertEquals(List.of(), messagePage.getContent());
        Mockito.verify(messageRepo, Mockito.times(1)).findAll(paging);

    }

    @Test
    public void getAllMessagesByUserTest() throws UserNotFoundException {
        User user = createUser(createUserDto());

        Mockito.when(userRepo.findByUuid(user.getUuid())).thenReturn(Optional.of(user));
        Mockito.when(messageRepo.findByUser(user)).thenReturn(List.of());

        assertEquals(List.of(), messageService.getAllMessagesByUser(user.getUuid()));
        Mockito.verify(messageRepo, Mockito.times(1)).findByUser(user);
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

    private MessageDto createMessageDto() {
        MessageDto messageDto = new MessageDto();
        messageDto.setHead("testhead");
        messageDto.setText("Some text for test");
        return messageDto;
    }
}
