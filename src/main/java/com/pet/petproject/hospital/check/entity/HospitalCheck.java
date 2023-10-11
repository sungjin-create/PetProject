package com.pet.petproject.hospital.check.entity;

import com.pet.petproject.hospital.hospital.entity.Hospital;
import com.pet.petproject.pet.entity.Pet;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class HospitalCheck {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "hospital_check_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "hopital_id")
  private Hospital hospital;

  @ManyToOne
  @JoinColumn(name = "pet_id")
  private Pet pet;
  private LocalDateTime visitDay;
  private LocalDateTime alarmTime;
  private boolean alarmCheck;
  private boolean visitCheck;
  private LocalDateTime registerDate;
}
