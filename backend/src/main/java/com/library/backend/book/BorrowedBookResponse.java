package com.library.backend.book;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowedBookResponse {

    private Integer id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private double rate;
    private boolean returned;
    private boolean returnApproved;
}
