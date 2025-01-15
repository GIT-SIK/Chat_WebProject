package com.example.ws_back.chat;

import org.springframework.stereotype.Service;

import com.example.ws_back.chat.ChatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{
    private final ChatRepository chatRepository;
}
