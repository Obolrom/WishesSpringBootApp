package io.romix.demo.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TypingMessage {
  private Boolean isTyping;

  @NonNull
  private Long receiverId;
}
