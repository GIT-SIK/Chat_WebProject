package com.example.ws_back.frnd;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService{
	
	private final ModelMapper modelMapper;
	private final FriendRepository fr;
}
