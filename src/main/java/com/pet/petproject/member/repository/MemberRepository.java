package com.pet.petproject.member.repository;

import com.pet.petproject.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);

  Optional<Member> findByEmailAuthKey(String emailAuthKey);
}
