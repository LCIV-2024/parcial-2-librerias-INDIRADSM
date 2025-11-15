package com.example.libreria.controller;

import com.example.libreria.dto.BookResponseDTO;
import com.example.libreria.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/sync")
    public ResponseEntity<String> syncBooks() {
        bookService.syncBooksFromExternalApi();
        return ResponseEntity.ok("Libros sincronizados exitosamente desde la API externa");
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{externalId}")
    public ResponseEntity<BookResponseDTO> getBookByExternalId(@PathVariable Long externalId) {
        return ResponseEntity.ok(bookService.getBookByExternalId(externalId));
    }

    @PutMapping("/{externalId}/stock")
    public ResponseEntity<BookResponseDTO> updateStock(
            @PathVariable Long externalId,
            @RequestParam Integer stockQuantity) {
        return ResponseEntity.ok(bookService.updateStock(externalId, stockQuantity));
    }
}

