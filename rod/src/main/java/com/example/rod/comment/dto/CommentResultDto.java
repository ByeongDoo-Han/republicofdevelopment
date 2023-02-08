package com.example.rod.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResultDto<T> {

    private int currentPage;

    private T data;



}
