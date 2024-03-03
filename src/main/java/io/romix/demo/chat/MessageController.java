package io.romix.demo.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {
  private final MessageService messageService;

  @GetMapping("/private/users/{companionId}")
  public PageMessageResponse getMessages(
      @PathVariable Long companionId,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "20", required = false) Integer pageSize
  ) {
    return messageService.getMessages(companionId, PageRequest.of(page, pageSize));
  }
}
