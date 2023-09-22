package com.pet.petproject.member.service;

import com.pet.petproject.member.components.MailComponents;
import com.pet.petproject.member.entity.Member;
import com.pet.petproject.member.model.JoinMember;
import com.pet.petproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

  private final MemberRepository memberRepository;
  private final BCryptPasswordEncoder encoder;
  private final MailComponents mailComponents;

  @Value("${jwt.secret}")
  private String secretKey;

  private Long expiredMs = 1000 * 60 * 60l;

  //가입하기
  public void join(JoinMember parameter) {
    String name = parameter.getName();
    String email = parameter.getEmail();
    String password = parameter.getPassword();
    Optional<Member> optionalMember = memberRepository.findByEmail(email);

    //이미 가입한 사람인지 체크
    if (optionalMember.isPresent()) {
      throw new RuntimeException(email + "는 이미 있습니다.");
    }

    String uuid = UUID.randomUUID().toString();
    Member member = Member.builder()
        .name(name)
        .email(email)
        .password(encoder.encode(password))
        .emailAuthYn(false)
        .emailAuthKey(uuid)
        .registerDate(LocalDateTime.now())
        .build();

    memberRepository.save(member);

    sendEmailAuth(email, uuid);

  }

  //이메일 보내기
  private void sendEmailAuth(String email, String uuid) {
    String subject = "PetProject에 가입을 축하드립니다.";
    String text = "<p>PetProject에 가입을 축하드립니다.</p><p>아래 링크를 클릭하셔서 가입을 완료 하세요.</p>" +
        "<div><a href='http://localhost:8080/member/mail/auth?id=" + uuid + "'>가입완료</a></div>";
    mailComponents.sendMail(email, subject, text);
  }

  //이메일 인증 권한 부여하기
  public boolean emailAuthGranted(String uuid) {
    //이메일 인증키로 체크
    Optional<Member> opMember = memberRepository.findByEmailAuthKey(uuid);
    if (opMember.isEmpty()) {
      return false;
    }

    Member member = opMember.get();
    member.setEmailAuthYn(true);
    member.setEmailAuthDate(LocalDateTime.now());
    memberRepository.save(member);
    return true;
  }

  //유저 로드 및 권한부여
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    List<GrantedAuthority> grantedAuthorities = new LinkedList<>();
    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

    return new User(member.getEmail(), member.getPassword(), grantedAuthorities);
  }

  //이메일로 회원가입한 멤버인지 체크
  public boolean findEmail(String email) {
    Optional<Member> optionalMember = memberRepository.findByEmail(email);
    if (optionalMember.isEmpty()) {
      return false;
    }
    return true;
  }

  //이메일 권한여부 체크
  public boolean checkEmailAuth(String email) {
    Optional<Member> opMember = memberRepository.findByEmail(email);
    boolean emailAuthYn = opMember.get().isEmailAuthYn();
    if (!emailAuthYn) {
      return false;
    }
    return true;
  }

  //패스워드 일치하는지 체크
  public boolean checkPassword(String email, String password) {
    Optional<Member> optionalMember = memberRepository.findByEmail(email);
    String existPassword = optionalMember.get().getPassword();

    if (!encoder.matches(password, existPassword)) {
      return false;
    }
    return true;
  }
}
