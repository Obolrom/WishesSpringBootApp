package io.romix.demo.chat;

import io.romix.demo.entity.Message;
import io.romix.demo.entity.UserEntity;
import io.romix.demo.security.CustomPrincipal;
import io.romix.demo.service.UserService;
import io.romix.demo.websocket.MessageCreateRequest;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
  private final MessageRepository messageRepository;
  private final UserService userService;

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
        .receiverId(message.getReceiver().getId())
        .receiverUsername(message.getReceiver().getUsername())
        .createdAt(message.getCreatedAt())
        .modified(message.getModifiedAt() != null)
        .message(message.getMessage())
        .build();
  }

  @NonNull
  private CustomPrincipal getUserPrincipal() {
    return (CustomPrincipal) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();
  }
}

