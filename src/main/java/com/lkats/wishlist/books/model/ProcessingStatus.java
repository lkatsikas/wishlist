package com.lkats.wishlist.books.model;

/**
 * Enumerator for processing statuses.
 */
public enum ProcessingStatus {

    /**
     * Status for non-existing book entries.
     */
    NOT_FOUND,

    /**
     * Status for existing book entries.
     */
    FOUND,

    /**
     * Status for updating existing book entry.
     */
    UPDATE
}
