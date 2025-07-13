
package com.example.ws_back.admin;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ws_back.usr.User;
import com.example.ws_back.usr.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserMigrationService {

    private final UserRepository ur;
    private final BCryptPasswordEncoder pwEncoder;

    @Transactional
    public void encryptAllPlainPasswords() {
        List<User> users = ur.findAll();
        int mc = 0;
        
        log.warn("사용자 비밀번호 정보 암호화를 시작합니다.");

        for (User user : users) {
            String rawPw = user.getUserPw();
            System.out.print(user.getUserNickname() + " / " + user.getUserUuid());
            if (rawPw.startsWith("$2a$") || rawPw.startsWith("$2b$") || rawPw.startsWith("$2y$")) {
            	System.out.println(" / PASS " );
                continue;
            }
        	mc ++;
            System.out.println(" / MODIFY " );
            user.setUserPw(pwEncoder.encode(rawPw));
            ur.save(user);
        }
        System.out.println("변경된 사용자 수 : " + mc + "명");
        System.out.println("사용자 수 : " + users.size() + "명");
    }
}
