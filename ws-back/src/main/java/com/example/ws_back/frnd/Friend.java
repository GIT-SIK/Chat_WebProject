package com.example.ws_back.frnd;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	@Column(name = "RECEVIER_USER_ID")
	private String RecevierUserId;
	
	@Column(name ="FRIEND_STATUS")
	private String FriendStatus;
	
	@Column(name = "FRIEND_REQUESTED_AT")
	private String FriendRequestedAt;
	
	@Column(name = "FRIEND_ACCEPTED_AT")
	private String FriendAcceptedAt;
}
