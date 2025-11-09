package site.haechan.sns_backend.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import site.haechan.sns_backend.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
	@Column(nullable = false, unique = true, length = 50)
	private String username;

	@Column(nullable = false, length = 100)
	private String password;

	@Column(nullable = false, length = 50)
	private String realName;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(length = 255)
	private String profileImageUrl;

	@Column(length = 255)
	private String introduction;

	@Column(length = 255, nullable = false)
	private Long followerCount;

	@Column(length = 255, nullable = false)
	private Long followingCount;

	@Column(nullable = false, columnDefinition = "TINYINT(1) default 0")
	private Boolean isSecret = false;
}
