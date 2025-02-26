/* DB : Mongo */

package com.example.ws_back.chat;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMongoRepository extends MongoRepository<Chat, String> {
	 List<Chat> findByRoomIdOrderByDateDesc(String roomId);

}

