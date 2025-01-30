package com.example.ws_back.usr;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_USER_MA")
@DynamicInsert
public class User {
    @Id
    @Column(name = "USER_ID")
    private String userId;
    
    @Column(name = "USER_NICKNAME")
    private String userNickName;

    @Column(name ="USER_PW")
    private String userPw;
    
    @Column(name ="USER_CREATED_AT")
    private String userCreatedAt;
    
    @Column(name ="IS_ADMIN")
    private String isAdmin;
    
    @Column(name ="IS_PUBLIC")
    private String isPublic;
    
   
}
