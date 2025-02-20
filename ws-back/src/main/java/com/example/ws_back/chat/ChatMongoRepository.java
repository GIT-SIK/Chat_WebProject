/* DB : Mongo */

package com.example.ws_back.chat;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.ws_back.chat.ChatMessage;

public interface ChatMongoRepository extends MongoRepository<ChatMessage, String> {

}

