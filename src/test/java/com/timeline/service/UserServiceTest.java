package com.timeline.service;

import com.timeline.dto.UserDto;
import com.timeline.exception.UserAlreadyExistException;
import com.timeline.model.User;
import com.timeline.repo.UserRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    @Test
    public void addUserTest() throws UserAlreadyExistException {
        UserDto userDto = createUserDto();
        User user = createUser(userDto);

        Mockito.when(userRepo.save(Mockito.any())).thenReturn(user);
        Mockito.when(userRepo.findByLogin(userDto.getLogin().toLowerCase())).thenReturn(Optional.empty());

        userService.addUser(userDto);

        assertNotNull(user.getUuid());
        assertNotNull(user.getPassword());
        assertEquals(userDto.getLogin(), user.getLogin());
        Mockito.verify(userRepo, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void userAlreadyExistExceptionTest() {
        UserDto userDto = createUserDto();

        Mockito.when(userRepo.save(Mockito.any())).thenReturn(Mockito.any());
        Mockito.when(userRepo.findByLogin(userDto.getLogin().toLowerCase())).thenReturn(Optional.of(new User()));

        Assert.assertThrows(UserAlreadyExistException.class, () -> userService.addUser(userDto));
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