package com.example.ws_back.usr;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository ur;
	private final ModelMapper modelMapper;
	
	/**
	 * 회원가입 처리
	 * 
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
	 *  
	 *  type : boolean 
	 *   */
	public boolean isNickValid(String nickname) {
		return ur.findByUserNickName(nickname);
	}
	public boolean isIdValid(String id) {
		return ur.findByUserId(id);
	}
}
