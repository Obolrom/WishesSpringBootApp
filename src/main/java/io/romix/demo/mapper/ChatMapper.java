package io.romix.demo.mapper;

import io.romix.demo.controller.dto.ChatPageResponse;
import io.romix.demo.controller.dto.ChatResponse;
import io.romix.demo.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

  @NonNull
  public ChatResponse toChatResponse(@NonNull Chat chat) {
    return ChatResponse.builder()
        .id(chat.getId())
        .title(chat.getTitle())
        .build();
  }

  @NonNull
  public ChatPageResponse toChatPageResponse(@NonNull Page<Chat> chatPage) {
    return ChatPageResponse.builder()
        .chats(chatPage.get().map(this::toChatResponse).toList())
        .total(chatPage.getTotalElements())
        .totalPages(chatPage.getTotalPages())
        .build();
  }
}
