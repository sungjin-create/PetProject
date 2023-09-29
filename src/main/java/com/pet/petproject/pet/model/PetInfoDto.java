package com.pet.petproject.pet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pet.petproject.pet.entity.Pet;
import com.pet.petproject.pet.entity.Status;
import java.util.Date;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetInfoDto {

  private Long petId;
  private String petName;
  private String imageKey;
  private String imageUrl;
  private Status status;

  private Integer birthYear;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;
  private LocalDateTime deleteDate;

  public static PetInfoDto of(Pet pet) {
    return PetInfoDto.builder()
        .petId(pet.getPetId())
        .petName(pet.getPetName())
        .imageKey(pet.getImageKey())
        .imageUrl(pet.getImageUrl())
        .status(pet.getStatus())
        .birthYear(pet.getBirthYear())
        .registerDate(pet.getRegisterDate())
        .updateDate(pet.getUpdateDate())
        .deleteDate(pet.getDeleteDate())
        .build();
  }

}
