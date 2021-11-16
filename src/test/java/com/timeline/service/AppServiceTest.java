package com.timeline.service;

import com.timeline.dto.MessageDto;
import com.timeline.dto.UserDto;
import com.timeline.exception.UserAlreadyExistException;
import com.timeline.exception.UserNotFoundException;
import com.timeline.model.Message;
import com.timeline.model.User;
import com.timeline.repo.MessageRepo;
import com.timeline.repo.UserRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

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
        UUID uuid = appService.addUser(userDto);
        user.setUuid(uuid);

        assertNotNull(uuid);
        assertNotNull(user.getPassword());
        assertEquals(userDto.getLogin(), user.getLogin());

        Mockito.verify(userRepo, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void userExistTest() {
        UserDto userDto = createUserDto();
        Mockito.when(userRepo.save(Mockito.any())).thenReturn(Mockito.any());
        Mockito.when(userRepo.findByLogin(userDto.getLogin().toLowerCase())).thenReturn(new User());

        Assert.assertThrows(UserAlreadyExistException.class, () -> appService.addUser(userDto));
    }

    @Test
    public void addMessageTest() throws UserNotFoundException, UserAlreadyExistException {
        UserDto userDto = createUserDto();
        User user = createUser(userDto);
        MessageDto messageDto = createMessageDto();
        Mockito.when(messageRepo.save(Mockito.any())).thenReturn(Mockito.any());

        assertNotNull(user.getUuid());
        appService.addMessage(user.getUuid(), messageDto);

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(messageRepo, Mockito.times(1)).save(Mockito.any());
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
}
