package com.lkats.wishlist.books.repository;

import com.lkats.wishlist.books.repository.domain.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Books repository operations.
 */
interface BooksRepository extends ReactiveMongoRepository<Book, String> {
}
