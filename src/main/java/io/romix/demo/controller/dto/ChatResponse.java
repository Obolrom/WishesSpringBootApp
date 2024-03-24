package io.romix.demo.controller.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
  private Long id;

  private String title;
}
