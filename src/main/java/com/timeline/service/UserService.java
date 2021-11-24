package com.timeline.service;

import com.timeline.dto.UserDto;
import com.timeline.exception.UserAlreadyExistException;
import com.timeline.model.User;
import com.timeline.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UUID addUser(UserDto userDTO) throws UserAlreadyExistException {
        if (userRepo.findByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new UserAlreadyExistException();
        } else {
            User user = new User(userDTO);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            return userRepo.save(user).getUuid();
        }
    }
}
