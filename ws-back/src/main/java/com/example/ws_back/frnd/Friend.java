package com.example.ws_back.frnd;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
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
@Table(name = "TB_FRIEND_MA")
public class Friend {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FriendSeq")
	@SequenceGenerator(sequenceName = "friend_sequence", name = "FriendSeq", allocationSize = 1)
	@Column(name = "SEQ")
	private Long Seq;
	
	@Column(name = "SENDER_USER_ID")
	private String SenderUserId;

	@Column(name = "RECEIVER_USER_ID")
	private String ReceiverUserId;
	
	@Column(name ="FRIEND_STATUS")
	private String FriendStatus;
	
	@Column(name = "FRIEND_REQUESTED_AT")
	private LocalDateTime FriendRequestedAt;
	
	@Column(name = "FRIEND_ACCEPTED_AT")
	private LocalDateTime FriendAcceptedAt;
	
    @PrePersist
    public void prePersist() {
        if (this.FriendRequestedAt == null) {
            this.FriendRequestedAt = LocalDateTime.now();
        }
    }
}
