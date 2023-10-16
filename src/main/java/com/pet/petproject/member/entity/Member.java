package com.pet.petproject.member.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

  @Id
  @Column(name = "member_id")
  private String id;
  private String name;
  private String password;

  private String email;
  private boolean emailAuthYn;
  private LocalDateTime emailAuthDate;
  private String emailAuthKey;

  private String socialId;
  @Enumerated(value = EnumType.STRING)
  private SocialType socialType;

  private LocalDateTime registerDate;
  private LocalDateTime deleteDate;

}












