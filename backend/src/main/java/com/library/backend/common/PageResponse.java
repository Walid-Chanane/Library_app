package com.library.backend.common;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {

    private List<T> content; //the list of elements ex. books (from findAll())
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first; // just to know if the page is the first or the last
    private boolean last;
}