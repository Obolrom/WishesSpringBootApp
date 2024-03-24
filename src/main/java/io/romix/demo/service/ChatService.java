package io.romix.demo.service;

import io.romix.demo.CustomException;
import io.romix.demo.controller.dto.ChatCreateRequest;
import io.romix.demo.controller.dto.ChatPageResponse;
import io.romix.demo.controller.dto.ChatResponse;
import io.romix.demo.entity.Chat;
import io.romix.demo.mapper.ChatMapper;
import io.romix.demo.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
  private final ChatRepository chatRepository;
  private final ChatMapper chatMapper;

  public ChatPageResponse getChats(int page, int pageSize) {
    PageRequest pageRequest = PageRequest.of(page, pageSize);

    Page<Chat> chatsPage = chatRepository.findAll(pageRequest);

    return chatMapper.toChatPageResponse(chatsPage);
  }

  public ChatResponse createChat(@NonNull ChatCreateRequest chatCreateRequest) {
    Chat chat = Chat.builder()
        .title(chatCreateRequest.getTitle())
        .build();

    return chatMapper.toChatResponse(chatRepository.save(chat));
  }

  public Chat getChatByIdOrError(@NonNull Long chatId) {
    return chatRepository
        .findById(chatId)
        .orElseThrow(() -> new CustomException(
            String.format("Chat with id %d not found", chatId),
            HttpStatus.NOT_FOUND));
  }
}
