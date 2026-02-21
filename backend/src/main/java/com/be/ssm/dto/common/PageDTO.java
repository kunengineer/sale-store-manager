package com.be.ssm.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class PageDTO<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}