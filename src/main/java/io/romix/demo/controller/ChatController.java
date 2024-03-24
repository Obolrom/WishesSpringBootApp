package io.romix.demo.controller;

import io.romix.demo.controller.dto.ChatCreateRequest;
import io.romix.demo.controller.dto.ChatPageResponse;
import io.romix.demo.controller.dto.ChatResponse;
import io.romix.demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {
  private final ChatService chatService;

  @GetMapping
  public ChatPageResponse getChats(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "20", required = false) Integer pageSize) {
    return chatService.getChats(page, pageSize);
  }

  @PostMapping
  public ChatResponse createChat(@RequestBody ChatCreateRequest chatCreateRequest) {
    return chatService.createChat(chatCreateRequest);
  }
}
