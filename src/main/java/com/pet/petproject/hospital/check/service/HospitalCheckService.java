package com.pet.petproject.hospital.check.service;

import com.pet.petproject.common.exception.AppException;
import com.pet.petproject.hospital.check.entity.HospitalCheck;
import com.pet.petproject.hospital.check.model.HospitalCheckDto;
import com.pet.petproject.hospital.check.repository.HospitalCheckRepository;
import com.pet.petproject.hospital.hospital.entity.Hospital;
import com.pet.petproject.pet.entity.Pet;
import com.pet.petproject.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalCheckService {

  private final PetRepository petRepository;
  private final HospitalCheckRepository hospitalCheckRepository;

  private static final int DAY_CALCULATION = 24 * 60 * 60;
  private static final int INQUIRY_PERIOD = 7;

  public void registerHospitalCheck(Hospital hospital) {
    LocalDateTime startDateTime = hospital.getStartDay().atStartOfDay();
    LocalDateTime endDateTime = hospital.getEndDay().atStartOfDay();

    Duration duration = Duration.between(startDateTime, endDateTime);

    long hospitalCycle = hospital.getHospitalCycle();
    long diffDay = getDiffDay(duration, hospitalCycle);

    List<HospitalCheck> hospitalCheckList = new ArrayList<>();

    LocalDateTime now = LocalDateTime.now();

    for (long i = 0; i <= diffDay; i++) {
      LocalDateTime visitDay = startDateTime.plusDays(i * hospitalCycle);
      LocalDateTime alarmTime = visitDay.minusDays(hospital.getBeforeAlarmDate())
          .plusHours(12);

      hospitalCheckList.add(
          HospitalCheck.builder()
              .hospital(hospital)
              .pet(hospital.getPet())
              .visitDay(visitDay)
              .alarmTime(alarmTime)
              .visitCheck(false)
              .registerDate(now)
              .build()
      );
    }
    hospitalCheckRepository.saveAll(hospitalCheckList);
  }

  private static long getDiffDay(Duration duration, long feedCycleDay) {
    return duration.getSeconds() / DAY_CALCULATION / feedCycleDay;
  }

  @Transactional
  public void deleteHospitalCheck(Hospital hospital) {
    hospitalCheckRepository.deleteAllByHospital(hospital);
  }

  public Page<HospitalCheckDto> getHospitalCheck(Long petId, Pageable pageable) {
    Pet pet = petRepository.findById(petId).orElseThrow(
        () -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Pet"));

    LocalDateTime startDay = LocalDateTime.now();
    LocalDateTime endDay = startDay.plusDays(INQUIRY_PERIOD);

    Page<HospitalCheck> hospitalChecks = hospitalCheckRepository
        .findAllByPetAndVisitDayBetween(pet, startDay, endDay, pageable);

    return HospitalCheckDto.of(hospitalChecks);
  }

  //현재시각 기준으로 지난 시간동안 check하지 않았던 목록 가져오기
  public Page<HospitalCheckDto> getMissHospitalCheck(Long petId, Pageable pageable) {

    Pet pet = petRepository.findById(petId).orElseThrow(
        () -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Pet"));

    Page<HospitalCheck> hospitalChecks = hospitalCheckRepository.findAllByPetAndVisitCheckIsFalseAndVisitDayBefore(
        pet, LocalDateTime.now(), pageable);

    return HospitalCheckDto.of(hospitalChecks);
  }

  @Transactional
  public void hospitalVisitCheck(Long hospitalCheckId) {
    hospitalCheckRepository.findById(hospitalCheckId).orElseThrow(
            () -> new AppException(HttpStatus.BAD_REQUEST, "Not Found HospitalCheck"))
        .setVisitCheck(true);
  }
}
