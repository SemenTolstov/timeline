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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AppService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UUID addUser(UserDto userDTO) throws UserAlreadyExistException {
        User userFromDb = userRepo.findByLogin(userDTO.getLogin().toLowerCase());
        if (userFromDb != null) {
            throw new UserAlreadyExistException();
        } else {
            User user = new User(userDTO);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userRepo.save(user);
            return user.getUuid();
        }
    }


    public void addMessage(UUID uuid, MessageDto messageDTO) throws UserNotFoundException {
        User userFromDb = checkUserForExistence(uuid);
        Message message = new Message(messageDTO);

        message.setUser(userFromDb);
        messageRepo.save(message);
    }

    public User checkUserForExistence(UUID uuid) throws UserNotFoundException {
        User userFromDb = userRepo.findByUuid(uuid);
        if (userFromDb == null) {
            throw new UserNotFoundException();
        }
        return userFromDb;
    }

    public Message checkMessageForExistence(Long messageId) throws MessageNotFoundException {

        Optional<Message> messageFromDb = messageRepo.findById(messageId);
        if (!messageFromDb.isPresent()) {
            throw new MessageNotFoundException();
        }
        return messageFromDb.get();
    }

    public void updateMessage(UUID uuid, Long messageId,
                              MessageDto messageDTO) throws UserNotFoundException,
            MessageNotFoundException, AccessErrorException {

        Message messageFromDb = checkMessageForExistence(messageId);
        if (!Objects.equals(checkUserForExistence(uuid).getId(), messageFromDb.getUser().getId())) {
            throw new AccessErrorException();
        }
        if (messageDTO.getHead() != null) messageFromDb.setHead(messageDTO.getHead());
        if (messageDTO.getText() != null) messageFromDb.setText(messageDTO.getText());
        messageRepo.save(messageFromDb);
    }

    public void deleteMessage(UUID uuid, Long messageId) throws UserNotFoundException,
            MessageNotFoundException, AccessErrorException {

        Message messageFromDb = checkMessageForExistence(messageId);
        if (!Objects.equals(checkUserForExistence(uuid).getId(), messageFromDb.getUser().getId())) {
            throw new AccessErrorException();
        }
        messageRepo.delete(messageFromDb);
    }

    public Page<Message> findAllMessages(int page, int size, SortDirection sortDirection) {
        Sort sortParam = Sort.by("added").descending();
        if (sortDirection == SortDirection.ASC) sortParam = Sort.by("added").ascending();
        Pageable paging = PageRequest.of(page, size, sortParam);
        Page<Message> messagePage = messageRepo.findAll(paging);

        return messagePage;
    }

    public List<Message> getAllMessagesByUser(UUID uuid) throws UserNotFoundException {
        User userFromDb = checkUserForExistence(uuid);
        List<Message> messages = messageRepo.findByUser(userFromDb.getId());
        return messages;
    }
}
