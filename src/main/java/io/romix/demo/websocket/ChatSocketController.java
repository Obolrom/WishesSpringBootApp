package io.romix.demo.websocket;

import io.romix.demo.chat.MessageResponse;
import io.romix.demo.chat.MessageService;
import io.romix.demo.security.CustomPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatSocketController {
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
        messageResponse.getReceiverUsername(), // should match with Principal name
        "/queue/chat", messageResponse);
    simpMessagingTemplate.convertAndSendToUser(
        messageResponse.getAuthorUsername(), // should match with Principal name
        "/queue/chat", messageResponse);

    log.info("Message sent: {}", messageResponse);
  }

  @MessageMapping("/direct/typing")
  public void typing(@NonNull @Payload TypingMessage typingMessage, Principal principal) {
    log.info("typing: {}", typingMessage);
    log.info("principal: {}", principal);

    simpMessagingTemplate.convertAndSendToUser(
        messageService.getUsernameById(typingMessage.getReceiverId()), // should match with Principal name
        "/queue/typing", new TypingResponse(typingMessage.getIsTyping()));
  }

  /**
   * We should use '/topic/room.{roomId}', not the '/topic/room/{roomId}'
   * @see <a href="https://github.com/spring-guides/gs-messaging-stomp-websocket/issues/35">github issue</a>
   */
  @MessageMapping("/room/{roomId}")
  public void room(
      @NonNull @Payload RoomMessageCreateRequest message,
      @DestinationVariable Long roomId,
      Principal principal) {
    log.info("roomId - message: {} - {}", roomId, message);

    CustomPrincipal customPrincipal =
        (CustomPrincipal) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

    MessageResponse messageResponse = messageService.createRoomMessage(roomId, message, customPrincipal);

    simpMessagingTemplate.convertAndSend("/topic/room." + roomId, messageResponse);

    log.info("Message sent: {}", messageResponse);
  }

  @MessageExceptionHandler
  public void handleException(Throwable exception) {
    log.error("Error handling message: {}", exception.getMessage());
  }
}
