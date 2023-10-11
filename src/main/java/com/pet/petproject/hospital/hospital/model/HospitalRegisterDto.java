package com.pet.petproject.hospital.hospital.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HospitalRegisterDto {

  private Long petId;
  private Integer hospitalCycle;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDay;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDay;
  private Integer beforeAlarmDate;
}
