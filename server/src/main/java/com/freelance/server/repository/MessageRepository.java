package com.freelance.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.freelance.server.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
 List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByTimestampAsc(
     Long senderId, Long receiverId, Long receiverId2, Long senderId2
 );
}
