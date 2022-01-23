package com.lkats.wishlist.books.repository;

import com.lkats.wishlist.books.repository.domain.Book;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.lkats.wishlist.books.model.ProcessingStatus.*;

/**
 * Provide service layer for Book operations.
 */
@Service
public class BooksRepositorySupport {

    private final BooksRepository booksRepository;
    public BooksRepositorySupport(final BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    /**
     * Updates data when when key (isbn) already exists,
     * inserts a new entry when key (isbn) is not present
     * No operation when key and the rest data are the same.
     * @param book the incoming book request
     * @return same, updated or newly created {@link Book}
     */
    public Mono<Book> insertOrUpdateOne(final Book book) {
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("isbn", ExampleMatcher.GenericPropertyMatchers.ignoreCase());
        Example<Book> image = Example.of(Book.builder().isbn(book.getIsbn()).build(), modelMatcher);

        return booksRepository
                .findOne(image)
                .flatMap(b -> {
                    if (book.equals(b)) {
                        return Mono.just(b.withProcessingStatus(FOUND));
                    }
                    return booksRepository.save(Book
                            .builder()
                            .id(b.getId())
                            .isbn(book.getIsbn())
                            .title(book.getTitle())
                            .author(book.getAuthor())
                            .processingStatus(UPDATE)
                            .build());
                })
                .switchIfEmpty(booksRepository.insert(book).map(c -> c.withProcessingStatus(NOT_FOUND)));
    }

    /**
     * Either delete existing id or
     * no op for not existing entries.
     * @param id the key to search with
     * @return either {@link Book} with process status
     * or process status only otherwise
     */
    public Mono<Book> deleteOne(final String id) {
        return booksRepository
                .findById(id)
                .flatMap(p -> booksRepository.deleteById(id).thenReturn(p.withProcessingStatus(FOUND)))
                .switchIfEmpty(Mono.just(Book.builder().processingStatus(NOT_FOUND).build()));
    }

    /**
     * Either retrieve existing book or
     * simply return NOT_FOUND.
     * @param id the key to search with
     * @return either {@link Book} with process status
     * or process status only otherwise
     */
    public Mono<Book> findById(final String id) {
        return booksRepository
                .findById(id)
                .map(book -> book.withProcessingStatus(FOUND))
                .switchIfEmpty(Mono.just(Book.builder().processingStatus(NOT_FOUND).build()));
    }

    /**
     * Return List(o..n) with elements found (no criteria).
     * @return list of {@link Book} elements
     */
    public Publisher<Book> findAll() {
        return booksRepository.findAll();
    }
}
