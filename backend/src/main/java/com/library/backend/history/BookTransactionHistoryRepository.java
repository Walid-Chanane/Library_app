package com.library.backend.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer> {

    @Query("""
        SELECT h FROM BookTransactionHistory h
        WHERE h.user.id = :userId
    """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);

    @Query("""
        SELECT h FROM BookTransactionHistory h
        WHERE h.book.owner.id = :userId
    """) // maybe we need to add "h.returned = true and/or h.returnApproved" later
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer userId);


    //WHERE h.bookId = :bookId AND h.returnedApproved = false
    @Query("""
    SELECT (COUNT(*) > 0) AS isBorrowed
    FROM BookTransactionHistory h
    WHERE h.user.id = :userIq
    AND h.book.id = :bookId
    AND h.returnApproved = false
    """)
    boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId);

    @Query("""
    SELECT h FROM BookTransactionHistory h
    WHERE h.book.id = :bookId
    AND h.user.id = :userId
    AND h.returned = false
    AND h.returnApproved = false
    """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(Integer bookId, Integer userId);
}
