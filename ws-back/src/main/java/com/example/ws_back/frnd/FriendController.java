package com.example.ws_back.frnd;

import org.springframework.stereotype.Controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FriendController {
	private final FriendService fs;

}
