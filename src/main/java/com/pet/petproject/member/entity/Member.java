package com.pet.petproject.member.entity;

import co.elastic.clients.util.DateTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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












