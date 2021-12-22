package com.timeline.controller;

import com.google.gson.Gson;
import com.timeline.dto.MessageDto;
import com.timeline.dto.UserDto;
import com.timeline.model.Message;
import com.timeline.model.User;
import com.timeline.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Test
    public void testGetAllMessages() throws Exception {
        Message message = createMessage(createMessageDto());
        Page<Message> messagePage = new PageImpl<>(List.of(message));

        when(messageService.findAllMessages(Mockito.any())).thenReturn(messagePage);

        mockMvc.perform(get("/timeline/messages")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(message.getId().intValue())))
                .andExpect(jsonPath("$.content[0].user", is(message.getUser())))
                .andExpect(jsonPath("$.content[0].head", is(message.getHead())))
                .andExpect(jsonPath("$.content[0].text", is(message.getText())))
                .andExpect(jsonPath("$.content[0].dateOfAddingAsUtc",
                        is(message.getDateOfAddingAsUtc().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))));
    }


    @Test
    public void testMessagesByUser() throws Exception {
        Message message = new Message(createMessageDto());
        List<Message> messages = List.of(message);
        User user = new User(createUserDto());

        when(messageService.getAllMessagesByUser(user.getUuid())).thenReturn(messages);

        mockMvc.perform(get("/timeline/users/{uuid}/messages", user.getUuid()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(messages.get(0).getId())))
                .andExpect(jsonPath("$[0].user", is(messages.get(0).getUser())))
                .andExpect(jsonPath("$[0].head", is(messages.get(0).getHead())))
                .andExpect(jsonPath("$[0].text", is(messages.get(0).getText())))
                .andExpect(jsonPath("$[0].dateOfAddingAsUtc",
                        is(messages.get(0).getDateOfAddingAsUtc().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))));
    }

    @Test
    public void testAddMessage() throws Exception {
        MessageDto messageDto = createMessageDto();
        Message message = new Message(messageDto);
        message.setId(1L);
        User user = new User(createUserDto());
        Gson gson = new Gson();

        when(messageService.addMessage(user.getUuid(), messageDto)).thenReturn(message.getId());

        mockMvc.perform(post("/timeline/users/{uuid}/messages", user.getUuid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(messageDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(message.getId().intValue())));
    }

    @Test
    public void testDeleteMessage() throws Exception {
        Long messageId = 1L;
        User user = new User(createUserDto());

        mockMvc.perform(delete("/timeline/users/{uuid}/messages/{messageId}", user.getUuid(), messageId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateMessage() throws Exception {
        MessageDto messageDto = createMessageDto();
        Message message = new Message(messageDto);
        message.setId(1L);
        User user = new User(createUserDto());
        Gson gson = new Gson();

        when(messageService.updateMessage(user.getUuid(), message.getId(), messageDto)).thenReturn(message.getId());

        mockMvc.perform(put("/timeline/users/{uuid}/messages/{messageId}", user.getUuid(), message.getId(), messageDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(messageDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(message.getId().intValue())));
    }

    private MessageDto createMessageDto() {
        MessageDto messageDto = new MessageDto();
        messageDto.setHead("testhead");
        messageDto.setText("Some text for test");
        return messageDto;
    }

    private Message createMessage(MessageDto messageDto) {
        Message message = new Message(messageDto);
        message.setUser(null);
        message.setId(1L);
        return message;
    }

    private UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setLogin("testlogin");
        userDto.setPassword("TestPassword123");
        return userDto;
    }
}
