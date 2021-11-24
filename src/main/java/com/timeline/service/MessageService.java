package com.timeline.service;

import com.timeline.dto.MessageDto;
import com.timeline.exception.AccessErrorException;
import com.timeline.exception.MessageNotFoundException;
import com.timeline.exception.UserNotFoundException;
import com.timeline.model.Message;
import com.timeline.model.User;
import com.timeline.repo.MessageRepo;
import com.timeline.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MessageRepo messageRepo;

    public Long addMessage(UUID uuid, MessageDto messageDTO) throws UserNotFoundException {
        User userFromDb = checkUserForExistence(uuid);
        Message message = new Message(messageDTO);
        message.setUser(userFromDb);
        return messageRepo.save(message).getId();
    }

    public Long updateMessage(UUID uuid, Long messageId,
                              MessageDto messageDTO) throws UserNotFoundException,
            MessageNotFoundException, AccessErrorException {

        Message messageFromDb = checkMessageForExistence(messageId);
        if (!Objects.equals(checkUserForExistence(uuid).getId(), messageFromDb.getUser().getId())) {
            throw new AccessErrorException();
        }
        messageFromDb.setHead(messageDTO.getHead());
        messageFromDb.setText(messageDTO.getText());
        return messageRepo.save(messageFromDb).getId();
    }

    public void deleteMessage(UUID uuid, Long messageId) throws UserNotFoundException,
            MessageNotFoundException, AccessErrorException {

        Message messageFromDb = checkMessageForExistence(messageId);
        if (!Objects.equals(checkUserForExistence(uuid).getId(), messageFromDb.getUser().getId())) {
            throw new AccessErrorException();
        }
        messageRepo.delete(messageFromDb);
    }

    public Page<Message> findAllMessages(Pageable pageable) {
        return messageRepo.findAll(pageable);
    }

    public List<Message> getAllMessagesByUser(UUID uuid) throws UserNotFoundException {
        User userFromDb = checkUserForExistence(uuid);
        return messageRepo.findByUser(userFromDb);
    }

    private User checkUserForExistence(UUID uuid) throws UserNotFoundException {
        return userRepo.findByUuid(uuid).orElseThrow(UserNotFoundException::new);
    }

    private Message checkMessageForExistence(Long messageId) throws MessageNotFoundException {
        return messageRepo.findById(messageId).orElseThrow(MessageNotFoundException::new);
    }
}
