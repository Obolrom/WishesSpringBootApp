package io.romix.demo.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageDtoV2 {
  private String message;

  private Long authorId;

  private Long receiverId;
}
