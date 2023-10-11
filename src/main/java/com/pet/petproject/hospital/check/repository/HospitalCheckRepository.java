package com.pet.petproject.hospital.check.repository;

import com.pet.petproject.hospital.check.entity.HospitalCheck;
import com.pet.petproject.hospital.hospital.entity.Hospital;
import com.pet.petproject.pet.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface HospitalCheckRepository extends JpaRepository<HospitalCheck, Long> {

  void deleteAllByHospital(Hospital hospital);

  Page<HospitalCheck> findAllByPetAndVisitDayBetween(Pet pet,
      LocalDateTime startDay, LocalDateTime endDay, Pageable pageable);

  Page<HospitalCheck> findAllByPetAndVisitCheckIsFalseAndVisitDayBefore(Pet pet, LocalDateTime now,
      Pageable pageable);

}
