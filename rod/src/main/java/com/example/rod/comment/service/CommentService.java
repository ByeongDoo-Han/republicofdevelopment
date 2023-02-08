package com.example.rod.comment.service;

import com.example.rod.answer.repository.AnswerRepository;
import com.example.rod.comment.dto.CommentRequestDto;
import com.example.rod.comment.dto.CommentResponseDto;
import com.example.rod.comment.dto.CommentResultDto;
import com.example.rod.comment.entity.CommentEntity;
import com.example.rod.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final AnswerRepository AnswerRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto createComment(Long answerId, CommentRequestDto commentRequestDto) {
        AnswerRepository.findById(answerId).orElseThrow(
                () -> new IllegalArgumentException("해당 답변이 존재하지 않습니다.")
        );

        CommentEntity saved = commentRepository.save(new CommentEntity(commentRequestDto.getContent()));

        return new CommentResponseDto(saved.getId(), saved.getContent());
    }

    @Transactional
    public CommentResponseDto updateComment(Long answerId, Long commentsId, CommentRequestDto commentRequestDto) {
        AnswerRepository.findById(answerId).orElseThrow(
                () -> new IllegalArgumentException("해당 답변이 존재하지 않습니다."));

        CommentEntity comment = commentRepository.findById(commentsId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다"));

        CommentEntity save = commentRepository.save(comment);
        return new CommentResponseDto(save.getId(), save.getContent());
    }

    @Transactional
    public void deleteComment(Long answerId, Long commentsId) {
        AnswerRepository.findById(answerId).orElseThrow(
                () -> new IllegalArgumentException("해당 답변이 존재하지 않습니다."));

        CommentEntity comment = commentRepository.findById(commentsId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다"));

        commentRepository.deleteById(comment.getId());

    }

//    public List<CommentResponseDto> getListComment() {
//        List<CommentEntity> commentAll = commentRepository.findAll();
//        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
//        for (CommentEntity comment : commentAll) {
//            commentResponseDtoList.add(new CommentResponseDto(comment.getId(), comment.getContent()));
//        }
//        return commentResponseDtoList;
//    }

    @Transactional(readOnly = true)
    public CommentResultDto getListComment(int offset, int limit) {
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "id"));
        Page<CommentEntity> page = commentRepository.findAll(pageRequest);
        Page<CommentResponseDto> map = page.map(commentEntity -> new CommentResponseDto(commentEntity.getId(), commentEntity.getContent()));
        List<CommentResponseDto> commentAll = map.getContent();
        long totalCount = map.getTotalElements();

        CommentResultDto resultDto = new CommentResultDto<>(offset,  commentAll);

        return resultDto;
    }
}
