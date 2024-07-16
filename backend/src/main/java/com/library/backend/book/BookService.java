package com.library.backend.book;

import com.library.backend.common.PageResponse;
import com.library.backend.exception.OperationNotPermittedException;
import com.library.backend.file.FileStorageService;
import com.library.backend.history.BookTransactionHistory;
import com.library.backend.history.BookTransactionHistoryRepository;
import com.library.backend.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository transactionHistoryRepository;
    private final FileStorageService fileStorageService;

    public Integer save(BookRequest request, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("Book " + bookId + " not found"));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdBy"/*book field*/).descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> responseBooks = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        //PageResponse is our custom Page class that's why we create a normal and complete Page class (above)
        // and then retrieve the needed fields from it to our PageResponse to return it
        return new PageResponse<>(
                responseBooks,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findBooksByOwner(int page, int size, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdBy").descending());
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pageable);
        List<BookResponse> responseBooks = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                responseBooks,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdBy").descending());
        Page<BookTransactionHistory> borrowedBooks = transactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBookResponse> responseBooks = borrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                responseBooks,
                borrowedBooks.getNumber(),
                borrowedBooks.getSize(),
                borrowedBooks.getTotalElements(),
                borrowedBooks.getTotalPages(),
                borrowedBooks.isFirst(),
                borrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdBy").descending());
        Page<BookTransactionHistory> returnedBooks = transactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> responseBooks = returnedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                responseBooks,
                returnedBooks.getNumber(),
                returnedBooks.getSize(),
                returnedBooks.getTotalElements(),
                returnedBooks.getTotalPages(),
                returnedBooks.isFirst(),
                returnedBooks.isLast()
        );
    }

    public Integer updateShareableStatus(Integer bookId, Authentication authenticatedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book " + bookId + " not found"));
        User user = (User) authenticatedUser.getPrincipal();
        if(Objects.equals(user.getId(), book.getOwner().getId())) {
            throw new OperationNotPermittedException("You can not update this book's shareable status!");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication authenticatedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book " + bookId + " not found"));
        User user = (User) authenticatedUser.getPrincipal();
        if(Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can not update this book's archived status!");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication authenticatedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book " + bookId + " not found"));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("You can not borrow this book!");
        }
        User user = (User) authenticatedUser.getPrincipal();
        if(Objects.equals(user.getId(), book.getOwner().getId())) {
            throw new OperationNotPermittedException("You can not borrow your own book!");
        }
        final boolean isAlreadyBorrowed = transactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if(isAlreadyBorrowed){
            throw new OperationNotPermittedException("This book is already borrowed!");
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer returnBorrowedBook(Integer bookId, Authentication authenticatedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow( () -> new EntityNotFoundException("Book " + bookId + " not found"));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("You can not return this book");
        }
        User user = (User) authenticatedUser.getPrincipal();
        if(Objects.equals(user.getId(), book.getOwner().getId())) {
            throw new OperationNotPermittedException("You can not return your own book");
        }
        BookTransactionHistory transactionHistory = transactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book"));
        transactionHistory.setReturned(true);
        return transactionHistoryRepository.save(transactionHistory).getId();
    }

    public Integer approveReturnedBook(Integer bookId, Authentication authenticatedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book " + bookId + " not found"));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("You can not approve the return of this book");
        }
        User user = (User) authenticatedUser.getPrincipal();
        if(!Objects.equals(user.getId(), book.getOwner().getId())) {
            throw new OperationNotPermittedException("You can not approve someone else's book");
        }
        BookTransactionHistory transactionHistory = transactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("This book is not returned, you can not approve it"));
        transactionHistory.setReturnApproved(true);
        return transactionHistoryRepository.save(transactionHistory).getId();
    }

    public void uploadBookCoverPicture(Integer bookId, MultipartFile file, Authentication authenticatedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book " + bookId + " not found"));
        User user = (User) authenticatedUser.getPrincipal();
        var bookCover = fileStorageService.saveFile(file, user.getId());
        book.setBookCover(bookCover);
        bookRepository.save(book);
    }
}
