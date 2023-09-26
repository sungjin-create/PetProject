package com.pet.petproject.login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinDto {

  @NotNull(message = "이름은 필수 값입니다.")
  private String name;

  @Email(message = "이메일 형식을 지켜주세요.")
  private String email;

  @NotNull(message = "비밀번호는 필수 값입니다.")
  private String password;
}
