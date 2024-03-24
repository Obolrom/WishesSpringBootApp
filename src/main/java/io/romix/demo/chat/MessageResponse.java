package io.romix.demo.chat;

import io.romix.demo.controller.dto.ChatResponse;
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

  private Long receiverId;

  private String receiverUsername;

  private Long createdAt;

  @NotNull
  private Boolean modified;

  private Long modifiedAt;

  private ChatResponse chat;
}
