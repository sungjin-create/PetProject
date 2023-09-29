package com.pet.petproject.pet.service;

import com.pet.petproject.common.awsS3.AwsS3;
import com.pet.petproject.common.awsS3.AwsS3Service;
import com.pet.petproject.common.exception.AppException;
import com.pet.petproject.member.entity.Member;
import com.pet.petproject.member.repository.MemberRepository;
import com.pet.petproject.pet.entity.Pet;
import com.pet.petproject.pet.model.PetInfoDto;
import com.pet.petproject.pet.model.UpdateDto;
import com.pet.petproject.pet.repository.PetRepository;
import com.pet.petproject.pet.model.RegisterDto;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import static com.pet.petproject.pet.entity.Status.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetService {

  private final MemberRepository memberRepository;
  private final PetRepository petRepository;
  private final AwsS3Service awsS3Service;

  private static final String UPLOAD_IMAGE_URL = "pet";

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
    if (petRepository.existsByMemberIdAndPetName(member, petName)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "중복된 이름의 펫이 있습니다.");
    }
  }

  //펫 등록하기
  private Pet registerPet(Member member, RegisterDto parameter) {

    Integer.getInteger(parameter.getBirthYear());

    return petRepository.save(Pet.builder()
        .memberId(member)
        .petName(parameter.getPetName())
        .birthYear(Integer.parseInt(parameter.getBirthYear()))
        .status(MANAGING)
        .registerDate(LocalDateTime.now())
        .build()
    );
  }

  //펫 업데이트 로직
  public PetInfoDto updatePetInfo(UpdateDto parameter) {
    //수정할 펫 찾기
    Pet pet = getPet(parameter.getBeforePetId());
    //수정 후 이름이 중복되는지 체크
    duplicateCheck(pet.getMemberId(), parameter.getUpdatePetName());
    //수정하기
    return PetInfoDto.of(updatePet(pet, parameter));
  }

  //pet 이름으로 가져오기
  private Pet getPet(Long beforePetId) {
    return petRepository.findByPetId(beforePetId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "이전 펫 정보가 없습니다."));
  }

  //pet 정보 업데이트
  private Pet updatePet(Pet pet, UpdateDto parameter) {
    pet.setPetName(parameter.getUpdatePetName());
    pet.setBirthYear(parameter.getUpdateBirthYear());
    pet.setUpdateDate(LocalDateTime.now());
    return petRepository.save(pet);
  }

  public PetInfoDto deletePetInfo(Long petId) {
    //펫 정보를 찾은후 임시 삭제
    return PetInfoDto.of(deletePet(getPet(petId)));
  }

  private Pet deletePet(Pet pet) {

    //임시 삭제
    pet.setDeleteDate(LocalDateTime.now());
    pet.setStatus(DELETE);
    return petRepository.save(pet);

  }

  public AwsS3 registerPetImage(Long petId, MultipartFile multipartFile) {

    Pet pet = getPet(petId);

    //이미 저장된 이미지가 있으면 S3저장소에서 삭제후 등록 진행
    if (pet.getImageKey() != null) {
      awsS3Service.remove(pet.getImageKey());
    }

    AwsS3 awsS3 = awsS3Service.awsImageUpload(multipartFile, UPLOAD_IMAGE_URL);

    saveImageKeyAndUrl(pet, awsS3);

    return awsS3;
  }

  private void saveImageKeyAndUrl(Pet pet, AwsS3 awsS3) {
    pet.setImageKey(awsS3.getKey());
    pet.setImageUrl(awsS3.getPath());
    petRepository.save(pet);
  }

  public void deletePetImage(Long petId) {
    Pet pet = petRepository.findByPetId(petId).orElseThrow(() ->
        new AppException(HttpStatus.BAD_REQUEST, "petId를 찾을 수 없습니다."));

    //key, url값으로 이미지 존재 여부 확인
    if (pet.getImageKey() == null) {
      throw new AppException(HttpStatus.BAD_REQUEST, "저장된 이미지가 없습니다.");
    }

    //awsS3 저장소에서 해당 키값을 가진 이미지 삭제
    awsS3Service.remove(pet.getImageKey());

    pet.setImageKey(null);
    pet.setImageUrl(null);
    petRepository.save(pet);
  }
}
