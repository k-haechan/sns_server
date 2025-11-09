package site.haechan.sns_backend.domain.comment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import site.haechan.sns_backend.domain.member.entity.Member;
import site.haechan.sns_backend.domain.post.entity.Post;
import site.haechan.sns_backend.global.common.entity.BaseEntity;

@Entity
public class Comment extends BaseEntity {

	@Column(nullable = false, length = 255)
	private String content;

	@Column(nullable = false)
	private Long likeCount = 0L;

	@Column(nullable = false)
	private Long reCommentCount = 0L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id", nullable = false)
	private Member author;
}
