package com.library.backend.feedback;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {

    private double evaluation;
    private String comment;
    private boolean ownFeedback; // to custom the user's own feedback on the user interface
}
