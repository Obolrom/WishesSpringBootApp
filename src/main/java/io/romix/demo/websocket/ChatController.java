package io.romix.demo.websocket;

import io.romix.demo.response.UserResponse;
import io.romix.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final UserService userService;
  private final SimpUserRegistry simpUserRegistry;

  @MessageMapping("/hello")
  @SendTo("/topic/greetings")
  public MessageDto greeting(@NonNull MessageDto message) {
    log.info("greeting: {}", message.getMessage());
    return new MessageDto(message.getMessage());
  }

  @MessageMapping("/direct")
  public void direct(@NonNull @Payload MessageDtoV2 message, Principal principal) {
    log.info("direct: {}", message);
    log.info("principal: {}", principal);

    simpMessagingTemplate.convertAndSendToUser(message.getReceiverId().toString(), "/queue/chat", message);
    simpMessagingTemplate.convertAndSendToUser(message.getAuthorId().toString(), "/queue/chat", message);

    log.info("Message sent: {}", message);
  }

  @MessageExceptionHandler
  public void handleException(Throwable exception) {
    log.error("Error handling message: {}", exception.getMessage());
  }
}
