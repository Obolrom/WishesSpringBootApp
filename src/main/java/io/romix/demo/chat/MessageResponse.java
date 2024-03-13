package io.romix.demo.chat;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
  @NotNull
  private Long id;

  private String message;

  @NotNull
  private Long authorId;

  @NotNull
  private String authorUsername;

  @NotNull
  private Long receiverId;

  @NotNull
  private String receiverUsername;

  private Long createdAt;

  @NotNull
  private Boolean modified;

  private Long modifiedAt;
}
