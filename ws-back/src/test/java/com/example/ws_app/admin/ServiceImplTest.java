package com.example.ws_app.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ws_back.WsAppApplication;
import com.example.ws_back.admin.UserMigrationService;

@SpringBootTest(classes = WsAppApplication.class)
public class ServiceImplTest {

	@Autowired
	private UserMigrationService ums;
    
	/* 사용자 암호화 용도 */
    @Test
    public void MigrationServiceMethod() {
            ums.encryptAllPlainPasswords();
        }
    }
