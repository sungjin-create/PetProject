package com.pet.petproject.pet.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.pet.petproject.common.exception.AppException;
import com.pet.petproject.member.entity.Member;
import com.pet.petproject.member.repository.MemberRepository;
import com.pet.petproject.pet.entity.Pet;
import com.pet.petproject.pet.model.PetInfoDto;
import com.pet.petproject.pet.model.UpdateDto;
import com.pet.petproject.pet.repository.PetRepository;
import com.pet.petproject.pet.model.RegisterDto;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetService {

  private final MemberRepository memberRepository;
  private final PetRepository petRepository;
  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  //펫 등록 로직
  public PetInfoDto registerPetInfo(String memberId, RegisterDto parameter) {
    //사용자 정보 확인
    Member member = getMember(memberId);

    //중복된 펫이름이 있는지 체크
    duplicateCheck(member, parameter.getPetName());

    //펫 등록하기
    return PetInfoDto.of(registerPet(member, parameter));
  }

  //멤버 정보 가져오기
  private Member getMember(String memberId) {
    return memberRepository.findById(memberId).orElseThrow(
        () -> new AppException(HttpStatus.BAD_REQUEST, "사용자 정보가 없습니다."));
  }

  //중복체크 - 멤버 아이디와, 펫 이름으로 중복여부 확인
  private void duplicateCheck(Member member, String petName) {
    if (petRepository.existsByMemberAndPetName(member, petName)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "중복된 이름의 펫이 있습니다.");
    }
  }

  //펫 등록하기
  private Pet registerPet(Member member, RegisterDto parameter) {

    Integer.getInteger(parameter.getBirthYear());

    return petRepository.save(Pet.builder()
        .member(member)
        .uuid(UUID.randomUUID().toString())
        .petName(parameter.getPetName())
        .birthYear(Integer.parseInt(parameter.getBirthYear()))
        .registerDate(LocalDateTime.now())
        .build()
    );
  }

  //펫 업데이트 로직
  public PetInfoDto updatePetInfo(UpdateDto parameter) {
    //수정할 펫 찾기
    Pet pet = getPet(parameter.getPetId());
    //수정 후 이름이 중복되는지 체크
    duplicateCheck(pet.getMember(), parameter.getUpdatePetName());
    //수정하기
    return PetInfoDto.of(updatePet(pet, parameter));
  }

  //pet 가져오기
  private Pet getPet(Long petId) {
    return petRepository.findById(petId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not found Pet"));
  }

  //pet 정보 업데이트
  private Pet updatePet(Pet pet, UpdateDto parameter) {
    pet.setPetName(parameter.getUpdatePetName());
    pet.setBirthYear(parameter.getUpdateBirthYear());
    pet.setUpdateDate(LocalDateTime.now());
    return petRepository.save(pet);
  }

  public PetInfoDto deletePetInfo(Long petId) {
    //펫을 찾은후 임시 삭제
    return PetInfoDto.of(deletePet(getPet(petId)));
  }

  private Pet deletePet(Pet pet) {

    //임시 삭제
    pet.setDeleteDate(LocalDateTime.now());
    return petRepository.save(pet);

  }

  @Transactional
  public void registerPetImage(Long petId, MultipartFile file) {

    Pet pet = getPet(petId);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(file.getContentType());
    metadata.setContentLength(file.getSize());

    String fileName = file.getOriginalFilename();
    String extension = getExtension(fileName);

    String fileUrl = pet.getUuid() + "." + extension;
    String key = "pet/" + fileUrl;

    try {
      // 존재할 경우 삭제
      if (amazonS3Client.doesObjectExist(bucket, key)) {
        amazonS3Client.deleteObject(bucket, key);
      }

      amazonS3Client.putObject(bucket, key, file.getInputStream(), metadata);
      pet.setImageUrl(key);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String getExtension(String fileName) {
    int extensionIndex = fileName.lastIndexOf(".");
    return fileName.substring(extensionIndex + 1);
  }

  @Transactional
  public void deletePetImage(Long petId) {
    Pet pet = getPet(petId);

    //key, url값으로 이미지 존재 여부 확인
    if (pet.getImageUrl() == null) {
      throw new AppException(HttpStatus.BAD_REQUEST, "저장된 이미지가 없습니다.");
    }

    //awsS3 저장소에서 해당 키값을 가진 이미지 삭제
    amazonS3Client.deleteObject(bucket, pet.getImageUrl());

    pet.setImageUrl(null);
  }
}
