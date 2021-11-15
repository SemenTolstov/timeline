package com.timeline.repo;

import com.timeline.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByLogin(String login);

    User findByUuid(UUID uuid);
}
