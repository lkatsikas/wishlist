package com.lkats.wishlist.books.controller;
import com.lkats.wishlist.books.repository.domain.Book;
import com.lkats.wishlist.books.repository.BooksRepositorySupport;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.lkats.wishlist.books.model.ProcessingStatus.NOT_FOUND;

/**
 * Books rest API.
 */
@RestController
@RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BooksController {

    private final BooksRepositorySupport booksRepositorySupport;

    /**
     * BooksController constructor using D.I.
     * @param booksRepositorySupport of type {@link BooksRepositorySupport}
     */
    public BooksController(final BooksRepositorySupport booksRepositorySupport) {
        this.booksRepositorySupport = booksRepositorySupport;
    }

    /**
     * Create a new book entry.
     * @param book of type {@link Book}
     */
    @PostMapping
    public Mono<ResponseEntity<Object>> insert(@RequestBody final Book book) {
        return booksRepositorySupport.insertOrUpdateOne(book)
                .map(p -> {
                    switch (p.getProcessingStatus()) {
                        case NOT_FOUND:
                            return ResponseEntity.created(URI.create("/" + p.getId())).build();
                        case UPDATE:
                            return ResponseEntity.status(HttpStatus.OK).build();
                        case FOUND:
                        default:
                            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
                    }
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteById(@PathVariable final String id) {
        return booksRepositorySupport
                .deleteOne(id)
                .map(f -> f.getProcessingStatus() == NOT_FOUND
                        ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                        : ResponseEntity.status(HttpStatus.OK).build());
    }

    @GetMapping
    public Publisher<Book> findAll() {
        return booksRepositorySupport.findAll();
    }

    @GetMapping("/{id}")
    public Publisher<ResponseEntity<Book>> findById(@PathVariable final String id) {
        return booksRepositorySupport
                .findById(id)
                .map(f -> f.getProcessingStatus() == NOT_FOUND
                        ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                        : ResponseEntity.status(HttpStatus.OK).build());
    }
}
