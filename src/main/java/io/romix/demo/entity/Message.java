package io.romix.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class Message implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "message")
  private String message;

  @ManyToOne
  @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
  private UserEntity receiver;

  @ManyToOne
  @JoinColumn(name = "author_id", referencedColumnName = "user_id", nullable = false)
  private UserEntity author;

  @Column(name = "created_at", nullable = false)
  private Long createdAt;

  @Column(name = "modified_at")
  private Long modifiedAt;

  @Column(name = "deleted", nullable = false)
  private Boolean deleted;

  @ManyToOne
  @JoinColumn(name = "chat_id", referencedColumnName = "id")
  private Chat chat;
}
