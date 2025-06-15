package com.example.ws_back.usr;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@DynamicUpdate
public class User {
    @Id
    @Column(name = "USER_ID")
    private String userId;
    
    @Column(name = "USER_NICKNAME")
    private String userNickname;
    
    @Column(name = "USER_UUID")
    private String userUuid;

    @Column(name ="USER_PW")
    private String userPw;
    
    @Column(name ="USER_CREATED_AT")
    private String userCreatedAt;
    
    @Column(name ="IS_ADMIN")
    private String isAdmin;
    
    @Column(name ="IS_PUBLIC")
    private String isPublic;
    
    @Column(name ="USER_CHAT_RECEIVE_SCOPE")
    private String userChatReceiveScope;
    
    @PrePersist
    @PreUpdate
    private void convertIdToLowerCase() {
        if (this.userId != null) {
            this.userId = this.userId.toLowerCase();
        }
    }
    
   
}
