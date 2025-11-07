package site.haechan.sns_backend.domain.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import site.haechan.sns_backend.domain.member.entity.Member;
import site.haechan.sns_backend.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {
	@Column(nullable = false, length = 500)
	private String title;

	@Column(nullable = false, length = 2000)
	private String content;

	@Column(nullable = false, length = 100)
	private Long likeCount = 0L;

	@ManyToOne
	@JoinColumn(name = "author_id", nullable = false)
	private Member author;
}
