package com.pet.petproject.member.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String password;
  private String email;
  private String provider;
  private String providerId;
  private boolean emailAuthYn;
  private LocalDateTime emailAuthDate;
  private String emailAuthKey;
  private Role role;

  private LocalDateTime registerDate;
  private LocalDateTime deleteDate;

}












