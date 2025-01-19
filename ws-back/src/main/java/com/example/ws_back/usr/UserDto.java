package com.example.ws_back.usr;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String userId;
    private String userNickName;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userPw;   
    private String userCreatedDt;
    private String isAdmin;
    private String isPublic;
    
}
