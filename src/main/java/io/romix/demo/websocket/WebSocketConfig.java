package io.romix.demo.websocket;

import io.romix.demo.CustomException;
import io.romix.demo.security.CustomPrincipal;
import io.romix.demo.security.JwtHandler;
import io.romix.demo.security.UserAuthenticationBearer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.*;
import java.util.Objects;
import java.util.regex.Pattern;

@Configuration
@Slf4j
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${spring.rabbitmq.host}")
  private String relayHost;

  @Value("${spring.rabbitmq.port}")
  private Integer relayPort;

  private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableStompBrokerRelay("/topic", "/queue")
        .setRelayHost(relayHost)
        .setRelayPort(relayPort);

    config.setApplicationDestinationPrefixes("/app", "/chat");
    config.setUserDestinationPrefix("/user");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/gs-guide-websocket");
//        .setHandshakeHandler(new CustomHandshakeHandler());
//        .setAllowedOrigins("*")
//        .withSockJS(); // <-- this line breaks mobile ws connection by some reason
  }

  @Override
  public void configureClientInboundChannel(@NonNull ChannelRegistration registration) {
    registration.interceptors(new ChannelInterceptor() {
      @Override
      public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor =
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        log.info("Headers: {}", accessor);

        assert accessor != null;

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
          String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
          assert authorizationHeader != null;
          String token = authorizationHeader.substring(7);

          JwtHandler jwtHandler = new JwtHandler(secret);
          JwtHandler.VerificationResult check = jwtHandler.check(token);
          Authentication authentication = UserAuthenticationBearer.create(check);

          SecurityContextHolder.getContext().setAuthentication(authentication);

          accessor.setUser(authentication);
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())
            && accessor.getDestination() != null
            && accessor.getDestination().startsWith("/user")) {
//          Long userId = Arrays.stream(accessor.getDestination().split("/"))
//              .filter(destinationToken -> isNumeric(destinationToken))
//              .findFirst()
//              .map(Long::parseLong)
//              .orElseThrow(() ->
//                  new CustomException("User id should be presented to listen"));

          CustomPrincipal principal =
              (CustomPrincipal) ((UsernamePasswordAuthenticationToken) Objects.requireNonNull(accessor.getUser())).getPrincipal();

          if (principal == null) {
            throw new CustomException("Something went wrong", HttpStatus.UNAUTHORIZED);
          }
//          if (!userId.equals(principal.getId())) {
//            throw new CustomException("Invalid user id to listen messages", HttpStatus.FORBIDDEN);
//          }

          log.info("STOMP SUBSCRIBE {} for principalId={}, sessionId={}", accessor.getDestination(), principal.getId(), accessor.getSessionId());
        }

        return message;
      }
    });
  }

  public boolean isNumeric(String strNum) {
    if (strNum == null) {
      return false;
    }
    return pattern.matcher(strNum).matches();
  }
}
