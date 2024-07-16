package com.library.backend.feedback;

import com.library.backend.book.Book;
import com.library.backend.book.BookRepository;
import com.library.backend.exception.OperationNotPermittedException;
import com.library.backend.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;

    public Integer save(FeedbackRequest request, Authentication authenticatedUser) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Book " + request.bookId() + " not found"));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("you are not permitted to give your feedback on this book!");
        }
        User user = (User) authenticatedUser.getPrincipal();
        if(Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("you can not rate your own book!");
        }
        Feedback feedback = feedbackMapper.toFeedback(request);
        return feedbackRepository.save(feedback).getId();
    }
}
