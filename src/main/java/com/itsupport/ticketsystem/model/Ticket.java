package com.itsupport.ticketsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;

  @Enumerated(EnumType.STRING)
  private Priority priority;

  @Enumerated(EnumType.STRING)
  private Category category;

  @Enumerated(EnumType.STRING)
  private Status status = Status.NEW;

  private LocalDateTime createdAt = LocalDateTime.now();

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public enum Priority {
    LOW, MEDIUM, HIGH
  }

  public enum Category {
    NETWORK, HARDWARE, SOFTWARE, OTHER
  }

  public enum Status {
    NEW, IN_PROGRESS, RESOLVED
  }
}
