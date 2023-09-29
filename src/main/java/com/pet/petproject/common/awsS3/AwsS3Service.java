package com.pet.petproject.common.awsS3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.pet.petproject.common.exception.AppException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class AwsS3Service {

  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private static final String IMAGE_SAVE_ROUTE = "/images/";

  public AwsS3 awsImageUpload(MultipartFile multipartFile) {
    //파일을 업로드하기 위해서 MultipartFile을 File객체로 변환
    File file = convertMultipartFileToFile(multipartFile)
        .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 변환 실패"));
    return uploadImage(file);
  }


  //key값과 path값 db에 저장한다.
  private AwsS3 uploadImage(File file) {
    String key = file.getName();
    String path = s3PutImage(file, key);

    //프로젝트 경로에 저장해둔 이미지를 삭제한다.
    removeFile(file);

    return AwsS3
        .builder()
        .key(key)
        .path(path)
        .build();
  }

  private String s3PutImage(File uploadFile, String fileName) {

    amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
        .withCannedAcl(CannedAccessControlList.PublicRead));

    return getS3Url(bucket, fileName);
  }

  private String getS3Url(String bucket, String fileName) {
    return amazonS3.getUrl(bucket, fileName).toString();
  }

  private void removeFile(File file) {

    if (!file.delete()) {
      log.error("file delete fail");
    }
  }


  //S3에 업로드 하기 위해 MultipartFile을 File객체로 변환 후 현재 프로젝트 경로에 업로드 한다.
  public Optional<File> convertMultipartFileToFile(MultipartFile multipartFile) {

    //현재 프로젝트 경로에 파일 세이브
    File file = saveNewFile(multipartFile);

    //새로운 파일에 이미지 주입
    createNewFile(file);
    fileWrite(multipartFile, file);
    return Optional.of(file);

  }

  private static File saveNewFile(MultipartFile multipartFile) {
    //UUID를 파일이름 앞에 붙여서 새로운 파일 이름 생성
    //내 프로젝트 경로 + images폴더에 파일 생성
    File file = new File(
        System.getProperty("user.dir") + IMAGE_SAVE_ROUTE + UUID.randomUUID() + multipartFile.getOriginalFilename());
    return file;
  }


  private static void createNewFile(File file) {
    try {
      file.createNewFile();
    } catch (IOException e) {
      throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "createNewFile IOException 오류");
    }
  }

  private static void fileWrite(MultipartFile multipartFile, File file) {
    try (FileOutputStream fos = new FileOutputStream(file)){
      fos.write(multipartFile.getBytes());
    } catch (IOException e) {
      throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "fileWrite 파일 저장 오류");
    }
  }


  public void remove(String key) {
    if (!amazonS3.doesObjectExist(bucket, key)) {
      throw new AmazonS3Exception("Amazon does not have key");
    }
    amazonS3.deleteObject(bucket, key);
  }
}
