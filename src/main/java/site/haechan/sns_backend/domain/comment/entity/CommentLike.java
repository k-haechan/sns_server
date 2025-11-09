package site.haechan.sns_backend.domain.comment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import site.haechan.sns_backend.domain.member.entity.Member;
import site.haechan.sns_backend.global.common.entity.BaseEntity;

@Entity
public class CommentLike extends BaseEntity {

	@ManyToOne(fetch =  FetchType.LAZY)
	@JoinColumn(name = "liker_id", nullable = false)
	private Member liker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_id", nullable = false)
	private Comment comment;
}
