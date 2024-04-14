package io.romix.demo.chat;

import io.romix.demo.entity.Chat;
import io.romix.demo.entity.Message;
import io.romix.demo.entity.UserEntity;
import io.romix.demo.mapper.ChatMapper;
import io.romix.demo.security.CustomPrincipal;
import io.romix.demo.service.ChatService;
import io.romix.demo.service.UserService;
import io.romix.demo.websocket.MessageCreateRequest;
import io.romix.demo.websocket.RoomMessageCreateRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
  private final MessageRepository messageRepository;
  private final UserService userService;
  private final ChatService chatService;
  private final ChatMapper chatMapper;

  public PageMessageResponse getMessages(
      @NonNull Long companionId, @NonNull PageRequest pageRequest) {

    Specification<Message> specification = getPrivateMessageSpecification(
        getUserPrincipal().getId(), companionId);

    Page<Message> messagesPage = messageRepository.findAll(
        specification,
        pageRequest.withSort(Sort.by("id").descending()));

    return PageMessageResponse.builder()
        .messages(messagesPage.map(this::toMessageResponse).getContent())
        .total(messagesPage.getTotalElements())
        .totalPages(messagesPage.getTotalPages())
        .build();
  }

  public PageMessageResponse getRoomMessages(
      @NonNull Long roomId, @NonNull PageRequest pageRequest) {
    Chat chat = chatService.getChatByIdOrError(roomId);

    Page<Message> messagesPage = messageRepository.findAllByChatId(
        chat.getId(),
        pageRequest.withSort(Sort.by("id").descending()));

    return PageMessageResponse.builder()
        .messages(messagesPage.map(this::toMessageResponse).getContent())
        .total(messagesPage.getTotalElements())
        .totalPages(messagesPage.getTotalPages())
        .build();
  }

  public MessageResponse createRoomMessage(
      @NonNull Long roomId,
      @NonNull RoomMessageCreateRequest roomMessageCreateRequest,
      @NonNull CustomPrincipal principal) {
    UserEntity author = userService.getUserByIdOrError(principal.getId());
    Chat chat = chatService.getChatByIdOrError(roomId);

    Message message = Message.builder()
        .author(author)
        .message(roomMessageCreateRequest.getMessage())
        .createdAt(System.currentTimeMillis())
        .deleted(false)
        .chat(chat)
        .build();

    return toMessageResponse(messageRepository.save(message));
  }

  @Transactional
  public MessageResponse createMessage(
      @NonNull MessageCreateRequest messageCreateRequest,
      @NonNull CustomPrincipal principal) {
    UserEntity author = userService.getUserByIdOrError(principal.getId());
    UserEntity receiver = userService.getUserByIdOrError(messageCreateRequest.getReceiverId());

    Message message = Message.builder()
        .author(author)
        .receiver(receiver)
        .message(messageCreateRequest.getMessage())
        .createdAt(System.currentTimeMillis())
        .deleted(false)
        .build();

    return toMessageResponse(messageRepository.save(message));
  }

  public String getUsernameById(@NonNull Long userId) {
    return userService.getUserByIdOrError(userId).getUsername();
  }

  @NonNull
  private Specification<Message> getPrivateMessageSpecification(
      @NonNull Long authorId, @NonNull Long receiverId) {
    return (root, query, criteriaBuilder) -> {
      Join<Message, UserEntity> authorJoin = root.join("author");
      Join<Message, UserEntity> receiverJoin = root.join("receiver");

      Predicate authorReceiver = criteriaBuilder.and(
          criteriaBuilder.equal(authorJoin.get("id"), authorId),
          criteriaBuilder.equal(receiverJoin.get("id"), receiverId)
      );
      Predicate receiverAuthor = criteriaBuilder.and(
          criteriaBuilder.equal(authorJoin.get("id"), receiverId),
          criteriaBuilder.equal(receiverJoin.get("id"), authorId)
      );

      return criteriaBuilder.or(authorReceiver, receiverAuthor);
    };
  }

  @NonNull
  private MessageResponse toMessageResponse(@NonNull Message message) {
    return MessageResponse.builder()
        .id(message.getId())
        .authorId(message.getAuthor().getId())
        .authorUsername(message.getAuthor().getUsername())
        .receiverId(Optional.ofNullable(message.getReceiver())
            .map(UserEntity::getId)
            .orElse(null))
        .receiverUsername(Optional.ofNullable(message.getReceiver())
            .map(UserEntity::getUsername)
            .orElse(null))
        .createdAt(message.getCreatedAt())
        .modified(message.getModifiedAt() != null)
        .message(message.getMessage())
        .chat(Optional.ofNullable(message.getChat())
            .map(chatMapper::toChatResponse)
            .orElse(null))
        .build();
  }

  @NonNull
  private CustomPrincipal getUserPrincipal() {
    return (CustomPrincipal) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();
  }
}

