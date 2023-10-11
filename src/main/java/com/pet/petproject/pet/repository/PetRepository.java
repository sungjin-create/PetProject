package com.pet.petproject.pet.repository;

import com.pet.petproject.member.entity.Member;
import com.pet.petproject.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

  boolean existsByMemberAndPetName(Member member, String petName);

  List<Pet> findAllByMember(Member member);

}