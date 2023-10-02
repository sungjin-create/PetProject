package com.pet.petproject.pet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDto {

  private Long petId;
  private String updatePetName;
  private Integer updateBirthYear;
  private String updateImgUrl;
}
