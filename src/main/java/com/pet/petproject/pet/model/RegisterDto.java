package com.pet.petproject.pet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class RegisterDto {

  private String petName;

  @Length(min = 4, max = 4, message = "년도는 4자리 수 입니다.")
  private String birthYear;

}
