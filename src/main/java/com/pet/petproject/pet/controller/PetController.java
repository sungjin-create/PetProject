package com.pet.petproject.pet.controller;

import com.pet.petproject.pet.model.PetInfoDto;
import com.pet.petproject.pet.model.RegisterDto;
import com.pet.petproject.pet.model.UpdateDto;
import com.pet.petproject.pet.service.PetService;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pet")
public class PetController {

  private final PetService petService;

  //펫 등록
  @PostMapping("/info/register")
  public ResponseEntity<?> registerPetInfo(@Valid @RequestBody RegisterDto parameter,
      Principal principal) {
    //멤버 아이디 가져오기
    String memberId = principal.getName();

    //펫 정보 저장
    PetInfoDto petInfoDto = petService.registerPetInfo(memberId, parameter);
    return ResponseEntity.ok().body(petInfoDto);
  }

  //펫 정보 업데이트
  @PutMapping("/info/update")
  public ResponseEntity<?> updatePetInfo(@RequestBody UpdateDto parameter) {
    return ResponseEntity.ok().body(petService.updatePetInfo(parameter));
  }

  //펫 정보 임시 삭제
  @DeleteMapping("/info")
  public ResponseEntity<?> deletePetInfo(Long petId) {
    return ResponseEntity.ok().body(petService.deletePetInfo(petId));
  }

  //aws, db에 image 등록
  @PostMapping("/info/aws/image/register")
  public ResponseEntity<?> registerPetImage(@RequestPart("file") MultipartFile multipartFile,
      @RequestParam("petId") Long petId) {
    petService.registerPetImage(petId, multipartFile);
    return ResponseEntity.ok().build();
  }

  //aws, db에 image 삭제
  @DeleteMapping("/info/aws/image/delete")
  public ResponseEntity<?> deletePetImage(@RequestParam("petId") Long petId) {
    petService.deletePetImage(petId);
    return ResponseEntity.ok().body("Image delete complete");
  }
}
