package site.haechan.sns_backend.domain.notification.entitty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import site.haechan.sns_backend.domain.member.entity.Member;
import site.haechan.sns_backend.domain.notification.type.NotificationType;
import site.haechan.sns_backend.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
	// 알림의 내용
	@Column(nullable = false, length = 255)
	private String message;

	// 알림을 받은 회원
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	// 알림의 상태 (읽음 여부)
	@Column(nullable = false)
	private boolean isRead;

	// 알림의 유형
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationType type;

	// 서브 ID (예: 게시글 ID, 댓글 ID 등)
	@Column(nullable = false)
	private Long subId;
}
