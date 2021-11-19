package com.timeline.controller;

import com.timeline.dto.UserDto;
import com.timeline.model.User;
import com.timeline.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    private UserService userService;

    @Test
    public void testRegistrationUserController() throws Exception {
        Mockito.when(userService.addUser(Mockito.any())).thenReturn(Mockito.isNotNull());

        UserDto userDto = createUserDto();
        ResponseEntity<String> responseEntity = userController.registrationUserAccount(userDto);

        assertEquals(200, responseEntity.getStatusCodeValue());
        Mockito.verify(userService, Mockito.times(1)).addUser(userDto);

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
