package io.romix.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('READ')")
public class CategoryController {

  @GetMapping
  public List<?> getCategories() {
    return Collections.emptyList();
  }
}
