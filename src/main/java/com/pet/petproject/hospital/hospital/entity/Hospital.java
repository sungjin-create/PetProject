package com.pet.petproject.hospital.hospital.entity;

import com.pet.petproject.hospital.hospital.model.HospitalRegisterDto;
import com.pet.petproject.pet.entity.Pet;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Hospital {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "hospital_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "pet_id")
  private Pet pet;

  private Integer hospitalCycle;
  private LocalDate startDay;
  private LocalDate endDay;
  private Integer beforeAlarmDate;

  private LocalDateTime registerDate;
  private LocalDateTime updateDate;
  private LocalDateTime deleteDate;

  public static Hospital of(HospitalRegisterDto parameter, Pet pet) {
    return Hospital.builder()
        .pet(pet)
        .hospitalCycle(parameter.getHospitalCycle())
        .startDay(parameter.getStartDay())
        .endDay(parameter.getEndDay())
        .beforeAlarmDate(parameter.getBeforeAlarmDate())
        .registerDate(LocalDateTime.now())
        .build();
  }

}
