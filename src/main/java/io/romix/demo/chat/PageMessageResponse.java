package io.romix.demo.chat;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageMessageResponse {
  private List<MessageResponse> messages;
  private Long total;
  private Integer totalPages;
}
