package com.timeline.repo;

import com.timeline.model.Message;
import com.timeline.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {

    Page<Message> findAll(Pageable pageable);

    List<Message> findByUser(Long user_id);

}
