package com.library.backend.book;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {

    private Integer id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String synopsis;
    private String ownerName;
    private double rate;
    private int ratedBy;
    private byte[] cover;
    private boolean archived;
    private boolean shareable;
}
