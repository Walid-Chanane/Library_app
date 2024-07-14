package com.library.backend.book;

import com.library.backend.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {


    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .author(request.author())
                .publisher(request.publisher())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .ownerName(book.getOwner().getFullName())
                .rate(book.getRate())
                .ratedBy(book.countFeedbacks())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                //.cover()
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory bookTransactionHistory) {
        return BorrowedBookResponse.builder()
                .id(bookTransactionHistory.getBook().getId())
                .title(bookTransactionHistory.getBook().getTitle())
                .author(bookTransactionHistory.getBook().getAuthor())
                .publisher(bookTransactionHistory.getBook().getPublisher())
                .isbn(bookTransactionHistory.getBook().getIsbn())
                .rate(bookTransactionHistory.getBook().getRate())
                .returned(bookTransactionHistory.isReturned())
                .returnApproved(bookTransactionHistory.isReturnApproved())
                .build();
    }
}
