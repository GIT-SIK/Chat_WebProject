package com.example.ws_back.service;

import org.springframework.stereotype.Service;

import com.example.ws_back.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{
    private final ChatRepository chatRepository;
}
