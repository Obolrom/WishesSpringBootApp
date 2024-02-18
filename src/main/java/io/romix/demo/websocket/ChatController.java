package io.romix.demo.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
@Slf4j
public class ChatController {

  @MessageMapping("/hello")
  @SendTo("/topic/greetings")
  public MessageDto greeting(@NonNull MessageDto message) {
    log.info("greeting: {}", message.getMessage());
    return new MessageDto(message.getMessage());
  }
}
