package com.library.backend.book;

import com.library.backend.common.BaseEntity;
import com.library.backend.feedback.Feedback;
import com.library.backend.history.BookTransactionHistory;
import com.library.backend.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {

    private String title;
    private String author;
    private String publisher;
    private String isbn; // International Standard Book Number
    private String synopsis; // book resume
    private String bookCover; // file path
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id") // by default hibernate will name it "owner_id"
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> TransactionHistory;

    @Transient
    public double getRate(){
        if(feedbacks == null){
            return 0.0;
        }
        var rate = feedbacks.stream()
                .mapToDouble(Feedback::getEvaluation)
                .average()
                .orElse(0.0);
        return Math.round(rate*10.0) / 10.0; // format -> ##.#
        // or -> return BigDecimal.valueOf(rate).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    @Transient
    public int countFeedbacks(){
        if(feedbacks == null){return 0;}
        return feedbacks.size();
    }
}
