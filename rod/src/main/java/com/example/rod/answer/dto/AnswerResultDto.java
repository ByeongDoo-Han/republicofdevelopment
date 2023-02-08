package com.example.rod.answer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResultDto<T> {

    private int currentPage;

    private Long answerCount;

    private T data;


}
