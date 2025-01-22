package com.example.ws_back.security;

import com.example.ws_back.usr.UserRepository;
import com.example.ws_back.usr.UserDto;
import com.example.ws_back.usr.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(id);
        if(user==null) {
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
        }
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return new CustomUserDetails(userDto);
    }

}
