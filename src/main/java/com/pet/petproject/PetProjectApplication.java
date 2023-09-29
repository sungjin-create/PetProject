package com.pet.petproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PetProjectApplication {
  static {
    System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
  }
  public static void main(String[] args) {
    SpringApplication.run(PetProjectApplication.class, args);
  }
}
