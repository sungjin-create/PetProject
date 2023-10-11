package com.pet.petproject.hospital.check.controller;

import com.pet.petproject.hospital.check.service.HospitalCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pet/hospital/check")
public class HospitalCheckController {

  private final HospitalCheckService hospitalCheckService;


  @PutMapping
  public ResponseEntity<?> hospitalVisitChecking(@RequestParam Long hospitalCheckId) {
    hospitalCheckService.hospitalVisitCheck(hospitalCheckId);
    return ResponseEntity.ok().build();
  }

  //현재시각으로부터 일주일간 방문 예정인 hospitalCheck 가져오기
  @GetMapping("/week")
  public ResponseEntity<?> getWeekHospitalCheck(@RequestParam Long petId,
      @PageableDefault(size = 10, sort = "visitDay", direction = Sort.Direction.ASC) Pageable pageable) {
    return ResponseEntity.ok().body(hospitalCheckService.getHospitalCheck(petId, pageable));
  }

  //현재시간을 기준으로 이전에 check하지않은 hospitalCheck 가져오기
  @GetMapping("/miss/last")
  public ResponseEntity<?> getMissHospitalCheck(@RequestParam Long petId,
      @PageableDefault(size = 10, sort = "visitDay", direction = Sort.Direction.ASC) Pageable pageable) {
    return ResponseEntity.ok().body(hospitalCheckService.getMissHospitalCheck(petId, pageable));
  }
}