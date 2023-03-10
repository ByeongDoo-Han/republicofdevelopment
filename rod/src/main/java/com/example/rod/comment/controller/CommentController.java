package com.example.rod.comment.controller;

import com.example.rod.comment.dto.CommentRequestDto;
import com.example.rod.comment.dto.CommentResponseDto;
import com.example.rod.comment.dto.CommentResultDto;
import com.example.rod.comment.service.CommentService;
import com.example.rod.comment.service.CommentServiceImpl;
import com.example.rod.security.details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/answers/{answerId}/comments")
    public void createComment
                (@PathVariable Long answerId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.createComment(answerId, commentRequestDto, userDetails);
    }

    // 댓글 수정
    @PutMapping("/answers/{answerId}/comments/{commentsId}")
    public void updateComment
              (@PathVariable Long answerId, @PathVariable Long commentsId, CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.updateComment(answerId, commentsId, commentRequestDto, userDetails);
    }

    // 댓글 삭제
    @DeleteMapping("/answers/{answerId}/comments/{commentsId}")
    public void deleteComment(@PathVariable Long answerId, @PathVariable Long commentsId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(answerId, commentsId, userDetails);
    }

    // 내 댓글 리스트조회 -> 필요한 기능인지 생각.
//    @GetMapping("/answers")
//    public List<CommentResponseDto> getListComment(@RequestParam int offset , @RequestParam int limit) {
//        return commentService.getListComment(offset, limit);
//    }
//    @GetMapping("/answers")
//    public CommentResultDto getListComment(@RequestParam int offset , @RequestParam int limit) {
//        return commentServiceImpl.getListComment(offset, limit);
//    }

}
