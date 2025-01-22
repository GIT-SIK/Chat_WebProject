package com.example.ws_back.usr;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ws_back.security.JwtUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository ur;
	private final ModelMapper modelMapper;
	private final JwtUtil jwtUtil;

	
	/**
     * 유저 데이터 반환
     * @Param : id
     * @Return : User -> UserDto
     */

    public UserDto findByUserId(String id){
        return modelMapper.map(ur.findByUserId(id),UserDto.class);
    }

    /**
     * 로그인 시 유저 데이터 반환
     * @Param : id, password
     * @Return : AccessToken, User -> UserDto
     */

    public LoginResponse login(String id, String password){

        User user = ur.findByUserId(id);
        if(user == null) {
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
        }

        if(!user.getUserPw().equals(password)) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        UserDto userDto = modelMapper.map(user,UserDto.class);

        String accessToken = jwtUtil.createAccessToken(userDto);
        return new LoginResponse(accessToken, userDto);
    }

    /**
     * Front로 반환될 데이터를 가공하는 클래스
     * @return : serviceToken, User
     *
     */
    public class LoginResponse {
        private String token;

        @JsonProperty("user")
        private UserDto userDto;

        public LoginResponse(String token, UserDto userDto) {
            this.token = token;
            this.userDto = userDto;
        }

        public String getToken(){
            return token;

        }

        public UserDto getUserDto(){
            return userDto;

        }
    }

	/**
	 * 회원가입 처리
	 * @param UserDto | UserDto -> User 변환 후 User를 저장
	 * @return Boolean | 회원가입 여부에 따른 true, false 반환
	 */
	public boolean signup(UserDto userDto) {	
		try {
			ur.save(modelMapper.map(userDto, User.class));
			return true;
		} catch (Exception e) {
			return false;
		}
	};
	
	
	/**
	 *  아이디 닉네임 여부
	 *  @param String | id, nickname 
	 *  @return Boolean | 각 값에 따라 값이 존재할 경우 true, 아니면 false
	 *   */
	public boolean isNickValid(String nickname) {
		return ur.existsByUserNickName(nickname);
	}
	public boolean isIdValid(String id) {
		return ur.existsByUserId(id);
	}
}
