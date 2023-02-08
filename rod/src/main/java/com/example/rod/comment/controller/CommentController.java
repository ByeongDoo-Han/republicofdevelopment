package com.example.rod.comment.controller;

import com.example.rod.comment.dto.CommentRequestDto;
import com.example.rod.comment.dto.CommentResponseDto;
import com.example.rod.comment.dto.CommentResultDto;
import com.example.rod.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/answers/{answerId}/comments")
    public CommentResponseDto createComment
                (@PathVariable Long answerId, @RequestBody CommentRequestDto commentRequestDto) {
        return commentService.createComment(answerId, commentRequestDto);
    }

    // 댓글 수정
    @PutMapping("/answers/{answerId}/comments/{commentsId}")
    public CommentResponseDto updateComment
              (@PathVariable Long answerId, @PathVariable Long commentsId, CommentRequestDto commentRequestDto) {
        return commentService.updateComment(answerId, commentsId, commentRequestDto);
    }

    // 댓글 삭제
    @DeleteMapping("/answers/{answerId}/comments/{commentsId}")
    public void deleteComment(@PathVariable Long answerId, @PathVariable Long commentsId) {
        commentService.deleteComment(answerId, commentsId);
    }

    // 내 댓글 리스트조회
//    @GetMapping("/answers")
//    public List<CommentResponseDto> getListComment(@RequestParam int offset , @RequestParam int limit) {
//        return commentService.getListComment(offset, limit);
//    }
    @GetMapping("/answers")
    public CommentResultDto getListComment(@RequestParam int offset , @RequestParam int limit) {
        return commentService.getListComment(offset, limit);
    }

}
