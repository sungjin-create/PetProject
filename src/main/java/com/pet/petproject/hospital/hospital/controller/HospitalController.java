package com.pet.petproject.hospital.hospital.controller;

import com.pet.petproject.hospital.hospital.model.HospitalRegisterDto;
import com.pet.petproject.hospital.hospital.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pet/hospital")
public class HospitalController {

  private final HospitalService hospitalService;

  /**
   * hospital param example
   * "petId" : 1,
   * "hospitalCycle" : 30, (30일 주기)
   * "startDate" : "2023-10-04",
   * "endDay" : "2024-11-20",
   * "beforeAlarmDate" : 2, (2일전에 알람)
   * 2023년 10월 04일 부터 2024년 11월 20일 30일마다
   * 병원주기적 방문 등록,
   * 알람 : 각 날짜 2일전 오후 12시에 전송
   */
  @PostMapping("/register")
  public ResponseEntity<?> registerHospital(@RequestBody HospitalRegisterDto hospitalRegisterDto) {

    hospitalService.registerHospital(hospitalRegisterDto);

    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<?> deleteHospital(@RequestParam("hospitalId") Long hospitalId) {
    hospitalService.deleteHospital(hospitalId);
    return ResponseEntity.ok().build();
  }

}
