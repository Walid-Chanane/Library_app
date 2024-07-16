package com.library.backend.feedback;

import com.library.backend.book.Book;
import org.springframework.stereotype.Service;

@Service
public class FeedbackMapper {

    public Feedback toFeedback(FeedbackRequest request){
        return Feedback.builder()
                .evaluation(request.evaluation())
                .comment(request.comment())
                .book(Book.builder()
                        .id(request.bookId())
                        .build())
                .build();
    }
}
