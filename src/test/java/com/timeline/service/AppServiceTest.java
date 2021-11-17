package com.timeline.service;

import com.timeline.dto.MessageDto;
import com.timeline.dto.SortDirection;
import com.timeline.dto.UserDto;
import com.timeline.exception.AccessErrorException;
import com.timeline.exception.MessageNotFoundException;
import com.timeline.exception.UserAlreadyExistException;
import com.timeline.exception.UserNotFoundException;
import com.timeline.model.Message;
import com.timeline.model.User;
import com.timeline.repo.MessageRepo;
import com.timeline.repo.UserRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AppServiceTest {

    @Autowired
    private AppService appService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private MessageRepo messageRepo;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void addUserTest() throws UserAlreadyExistException {
        UserDto userDto = createUserDto();
        User user = createUser(userDto);
        Mockito.when(userRepo.save(Mockito.any())).thenReturn(Mockito.any());
        Mockito.when(userRepo.findByLogin(userDto.getLogin().toLowerCase())).thenReturn(null);
        appService.addUser(userDto);

        assertNotNull(user.getUuid());
        assertNotNull(user.getPassword());
        assertEquals(userDto.getLogin(), user.getLogin());

        Mockito.verify(userRepo, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void userAlreadyExistExceptionTest() {
        UserDto userDto = createUserDto();
        Mockito.when(userRepo.save(Mockito.any())).thenReturn(Mockito.any());
        Mockito.when(userRepo.findByLogin(userDto.getLogin().toLowerCase())).thenReturn(new User());

        Assert.assertThrows(UserAlreadyExistException.class, () -> appService.addUser(userDto));
    }

    @Test
    public void addMessageTest_whenAddingIsCorrect() throws UserNotFoundException {
        UserDto userDto = createUserDto();
        User user = createUser(userDto);
        MessageDto messageDto = createMessageDto();

        Mockito.when(messageRepo.save(Mockito.any())).thenReturn(Mockito.any());
        Mockito.when(userRepo.findByUuid(user.getUuid())).thenReturn(user);

        appService.addMessage(user.getUuid(), messageDto);

        Mockito.verify(messageRepo, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void checkMessageForExistenceTest_whenNotFound() {
        MessageDto messageDto = createMessageDto();
        Message message = createMessage(messageDto);
        Mockito.when(messageRepo.findById(message.getId())).thenReturn(Optional.empty());

        Assert.assertThrows(MessageNotFoundException.class, () -> appService.checkMessageForExistence(message.getId()));
    }

    @Test
    public void checkUserForExistenceTest_whenNotFound() {
        UserDto userDto = createUserDto();
        User user = createUser(userDto);
        Mockito.when(userRepo.findByUuid(user.getUuid())).thenReturn(null);

        Assert.assertThrows(UserNotFoundException.class, () -> appService.checkUserForExistence(user.getUuid()));
    }

    @Test
    public void updateMessageTest() throws UserNotFoundException, MessageNotFoundException, AccessErrorException {
        User user = createUser(createUserDto());
        MessageDto messageDto = createMessageDto();
        Message message = new Message();
        message.setHead("testhead2");
        message.setText("Some text for second test");
        message.setUser(user);
        messageRepo.save(message);
        Mockito.when(messageRepo.save(Mockito.any())).thenReturn(Mockito.any());
        Mockito.when(messageRepo.findById(message.getId())).thenReturn(Optional.of(message));
        Mockito.when(userRepo.findByUuid(user.getUuid())).thenReturn(user);

        appService.updateMessage(user.getUuid(),message.getId(), messageDto);

        assertEquals(message.getHead(), messageDto.getHead());
        assertEquals(message.getText(), messageDto.getText());

        Mockito.verify(messageRepo, Mockito.times(2)).save(message);
    }

    @Test
    public void deleteMessageTest() throws UserNotFoundException, MessageNotFoundException, AccessErrorException {
        User user = createUser(createUserDto());
        Message message = new Message();
        message.setUser(user);
        messageRepo.save(message);

        Mockito.when(messageRepo.findById(message.getId())).thenReturn(Optional.of(message));
        Mockito.when(userRepo.findByUuid(user.getUuid())).thenReturn(user);

        appService.deleteMessage(user.getUuid(),message.getId());

        Mockito.verify(messageRepo, Mockito.times(1)).delete(message);
    }

    @Test
    public void findAllMessagesTest() {
        Pageable paging = PageRequest.of(0,10, Sort.by("dateOfAddingAsUtc").ascending());
        Mockito.when(messageRepo.findAll(paging)).thenReturn(Page.empty());

        Page<Message> messagePage = appService.findAllMessages(0,10, SortDirection.ASC);

        assertEquals(List.of(), messagePage.getContent());

        Mockito.verify(messageRepo, Mockito.times(1)).findAll(paging);

    }

    @Test
    public void getAllMessagesByUserTest() throws UserNotFoundException {
        User user = createUser(createUserDto());
        Mockito.when(userRepo.findByUuid(user.getUuid())).thenReturn(user);
        Mockito.when(messageRepo.findByUser(user.getId())).thenReturn(List.of());

        assertEquals(List.of(), appService.getAllMessagesByUser(user.getUuid()));

        Mockito.verify(messageRepo, Mockito.times(1)).findByUser(user.getId());
    }

    private UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setLogin("testlogin");
        userDto.setPassword("TestPassword123");
        return userDto;
    }

    private MessageDto createMessageDto() {
        MessageDto messageDto = new MessageDto();
        messageDto.setHead("testhead");
        messageDto.setText("Some text for test");
        return messageDto;
    }

    private User createUser(UserDto userDto) {
        User user = new User(userDto);
        assertNotNull(user);
        assertNotNull(user.getUuid());
        assertEquals(user.getLogin(), userDto.getLogin());
        return user;
    }

    private Message createMessage(MessageDto messageDto) {
        Message message = new Message(messageDto);
        message.setId(1L);
        assertNotNull(message);
        assertNotNull(message.getId());
        assertEquals(message.getHead(), messageDto.getHead());
        return message;
    }
}
