package com.pet.petproject.pet.model;

import com.pet.petproject.pet.entity.Pet;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetInfoDto {

  private String petId;
  private String petName;
  private String imageUrl;

  private Integer birthYear;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;
  private LocalDateTime deleteDate;

  public static PetInfoDto of(Pet pet) {
    return PetInfoDto.builder()
        .petId(pet.getPetId())
        .petName(pet.getPetName())
        .imageUrl(pet.getImageUrl())
        .birthYear(pet.getBirthYear())
        .registerDate(pet.getRegisterDate())
        .updateDate(pet.getUpdateDate())
        .deleteDate(pet.getDeleteDate())
        .build();
  }

}
