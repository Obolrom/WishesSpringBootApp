package io.romix.demo.controller.dto;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatPageResponse {
  private List<ChatResponse> chats;
  private Long total;
  private Integer totalPages;
}
