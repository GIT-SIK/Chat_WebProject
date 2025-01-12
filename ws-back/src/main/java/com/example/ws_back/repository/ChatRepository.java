package com.example.ws_back.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.ws_back.dto.WSMessage;

public interface ChatRepository extends MongoRepository<WSMessage, String> {

}

