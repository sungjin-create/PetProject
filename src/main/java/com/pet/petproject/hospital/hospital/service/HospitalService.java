package com.pet.petproject.hospital.hospital.service;

import com.pet.petproject.common.exception.AppException;
import com.pet.petproject.hospital.check.service.HospitalCheckService;
import com.pet.petproject.hospital.hospital.entity.Hospital;
import com.pet.petproject.hospital.hospital.model.HospitalRegisterDto;
import com.pet.petproject.hospital.hospital.repository.HospitalRepository;
import com.pet.petproject.pet.entity.Pet;
import com.pet.petproject.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HospitalService {

  private final HospitalRepository hospitalRepository;
  private final HospitalCheckService hospitalCheckService;
  private final PetRepository petRepository;

  @Transactional
  public void registerHospital(HospitalRegisterDto parameter) {

    Pet pet = petRepository.findById(parameter.getPetId()).orElseThrow(
        () -> new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Not Found Pet"));

    Hospital hospital = hospitalRepository.save(Hospital.of(parameter, pet));

    hospitalCheckService.registerHospitalCheck(hospital);

  }

  @Transactional
  public void deleteHospital(Long hospitalId) {
    Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow(
        () -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Hospital"));

    hospitalCheckService.deleteHospitalCheck(hospital);
    hospitalRepository.delete(hospital);
  }
}
