package io.romix.demo.controller;

import io.romix.demo.controller.entity.GetAllWishes;
import io.romix.demo.controller.entity.WishId;
import io.romix.demo.controller.entity.WishResponse;
import io.romix.demo.entity.WishEntity;
import io.romix.demo.service.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishes")
public class WishesController {

    private final WishService wishService;

    @Autowired
    public WishesController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<GetAllWishes> getAllWishes() {
        final var result = new GetAllWishes(wishService.getAllWishes());

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WishResponse> getWish(@PathVariable("id") Long id) {
        return wishService.findWishById(id)
                .map(wishEntity -> ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new WishResponse(wishEntity)))
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping
    public ResponseEntity<WishId> createWish(@RequestBody WishEntity wish) {
        final var createdWish = wishService.saveWish(wish);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new WishId(createdWish.getId()));
    }
}
