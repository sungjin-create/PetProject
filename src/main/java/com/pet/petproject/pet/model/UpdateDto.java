package com.pet.petproject.pet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
