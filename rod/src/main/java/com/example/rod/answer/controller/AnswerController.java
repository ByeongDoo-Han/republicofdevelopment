package com.example.rod.answer.controller;

import com.example.rod.answer.dto.AnswerRequestDto;
import com.example.rod.answer.dto.AnswerResponseDto;
import com.example.rod.answer.service.AnswerService;
import com.example.rod.comment.dto.CommentResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AnswerController {

    private final AnswerService answerService;

    // 답변 작성
    @PostMapping("/questions/answers")
    public AnswerResponseDto createAnswer(@RequestPart@RequestBody AnswerRequestDto answerRequestDto) {
        return answerService.createAnswer(answerRequestDto);
    }

    // 답변 수정
    @PutMapping("/answers/{answerId}")
    public AnswerResponseDto updateAnswer(@PathVariable Long answerId, AnswerRequestDto answerRequestDto) {
        return answerService.updateAnswer(answerId, answerRequestDto);
    }

    // 답변 삭제
    @DeleteMapping("/answers/{answerId}")
    public void deleteAnswer(@PathVariable Long answerId) {
        answerService.deleteAnswer(answerId);
    }

    @GetMapping("/answers")
    public CommentResultDto getListAnswer
    (@RequestParam(defaultValue = "1") int page,
      @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return answerService.getListAnswer(pageable, page);
    }


    // 내 답변 상세조회
    @GetMapping("/answers/{answerId}")
    public AnswerResponseDto getAnswer(@PathVariable Long answerId) {
        return answerService.getAnswer(answerId);
    }

}
