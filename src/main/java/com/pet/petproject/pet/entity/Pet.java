package com.pet.petproject.pet.entity;

import com.pet.petproject.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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
  @JoinColumn(name = "member_id")
  private Member member;
  private String petName;
  private String uuid;
  private Integer birthYear;
  private String imageUrl;

  private LocalDateTime registerDate;
  private LocalDateTime updateDate;
  private LocalDateTime deleteDate;

}
