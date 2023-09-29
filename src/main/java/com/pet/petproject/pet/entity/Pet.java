package com.pet.petproject.pet.entity;

import co.elastic.clients.util.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pet.petproject.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long petId;

  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "memberId")
  private Member memberId;
  private String petName;

  private Integer birthYear;
  private String imageKey;
  private String imageUrl;
  @Enumerated(value = EnumType.STRING)
  private Status status;

  private LocalDateTime registerDate;
  private LocalDateTime updateDate;
  private LocalDateTime deleteDate;

}
