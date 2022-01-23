package com.lkats.wishlist.books.repository.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lkats.wishlist.books.model.ProcessingStatus;
import lombok.*;
import lombok.experimental.Wither;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Book model representation.
 */
@Data
@Wither
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    /**
     * Id for the entity.
     */
    @Id
    private String id;

    /**
     * The title of the book.
     */
    private String title;

    /**
     * The author of the book.
     */
    private String author;

    /**
     * International standard book number.
     */
    private String isbn;

    /**
     * The processing status.
     * Transient field used only for processing.
     */
    @Transient
    @JsonIgnore
    private ProcessingStatus processingStatus;

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (!(obj instanceof Book)) {
            return false;
        }

        Book b = (Book) obj;
        return this.getAuthor().equals(b.getAuthor())
                && this.getIsbn().equals(b.getIsbn())
                && this.getTitle().equals(b.getTitle());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().hashCode();
    }
}
