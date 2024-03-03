package io.romix.demo.websocket;

import io.romix.demo.chat.MessageResponse;
import io.romix.demo.chat.MessageService;
import io.romix.demo.security.CustomPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final MessageService messageService;
  private final SimpUserRegistry simpUserRegistry;

  @MessageMapping("/hello")
  @SendTo("/topic/greetings")
  public MessageDto greeting(@NonNull MessageDto message) {
    log.info("greeting: {}", message.getMessage());
    return new MessageDto(message.getMessage());
  }

  @MessageMapping("/direct")
  public void direct(@NonNull @Payload MessageCreateRequest message, Principal principal) {
    log.info("direct: {}", message);
    log.info("principal: {}", principal);

    CustomPrincipal customPrincipal =
        (CustomPrincipal) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

    MessageResponse messageResponse = messageService.createMessage(message, customPrincipal);

    simpMessagingTemplate.convertAndSendToUser(
        messageResponse.getReceiverId().toString(),
        "/queue/chat", messageResponse);
    simpMessagingTemplate.convertAndSendToUser(
        messageResponse.getAuthorId().toString(),
        "/queue/chat", messageResponse);

    log.info("Message sent: {}", messageResponse);
  }

  @MessageExceptionHandler
  public void handleException(Throwable exception) {
    log.error("Error handling message: {}", exception.getMessage());
  }
}
