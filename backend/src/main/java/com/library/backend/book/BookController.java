package com.library.backend.book;

import com.library.backend.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Integer> saveBook(@Valid @RequestBody BookRequest request,
                                            Authentication authenticatedUser) { // ResponseEntity<Integer> we will return the id, u can make it void or w/e
        return ResponseEntity.ok(bookService.save(request, authenticatedUser));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable Integer bookId){
        return ResponseEntity.ok(bookService.findById(bookId));
    }

    @GetMapping//hibernate already provides a page response entity, but it's too much for us, so this is a custom page response entity.
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "page", defaultValue = "0"/*per default get the first page*/, required = false/*cuz we have the default value*/)int page,
            @RequestParam(name = "size", defaultValue = "10", required = false)int size, //elements in a single page
            Authentication authenticatedUser // render all books except the ones owned by the user
            ){
        return ResponseEntity.ok(bookService.findAllBooks(page, size, authenticatedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0"/*per default get the first page*/, required = false/*cuz we have the default value*/)int page,
            @RequestParam(name = "size", defaultValue = "10", required = false)int size, //elements in a single page
            Authentication authenticatedUser
    ){
        return ResponseEntity.ok(bookService.findBooksByOwner(page, size, authenticatedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false)int page,
            @RequestParam(name = "size", defaultValue = "10", required = false)int size,
            Authentication authenticatedUser
    ){
        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page, size, authenticatedUser));
    }

    @GetMapping("/returned")//BorrowedBookResponse contains what we need to return
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication authenticatedUser
    ){
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page, size, authenticatedUser));
    }

    @PatchMapping("/sareable/{bookId}")
    public ResponseEntity<Integer> updateShareableStatus(@PathVariable Integer bookId, Authentication authenticatedUser){
        return ResponseEntity.ok(bookService.updateShareableStatus(bookId, authenticatedUser));
    }
}
