package site.haechan.sns_backend.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import site.haechan.sns_backend.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByUsername(String username);
}
