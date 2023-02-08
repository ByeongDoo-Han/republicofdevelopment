package com.example.rod.comment.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String content;

    public CommentResponseDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
