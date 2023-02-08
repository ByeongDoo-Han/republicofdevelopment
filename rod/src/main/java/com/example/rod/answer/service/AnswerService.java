package com.example.rod.answer.service;

import com.example.rod.answer.dto.AnswerRequestDto;
import com.example.rod.answer.dto.AnswerResponseDto;
import com.example.rod.answer.entity.AnswerEntity;
import com.example.rod.answer.repository.AnswerRepository;
import com.example.rod.comment.dto.CommentResponseDto;
import com.example.rod.comment.dto.CommentResultDto;
import com.example.rod.comment.entity.CommentEntity;
import com.example.rod.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository AnswerRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public AnswerResponseDto createAnswer(AnswerRequestDto answerRequestDto) {
        AnswerEntity answerEntity1 = new AnswerEntity(answerRequestDto.getContent());
        AnswerEntity saved = AnswerRepository.save(answerEntity1);
        return new AnswerResponseDto(saved.getId(), saved.getContent(), answerEntity1.getLikes());
    }

//    @Transactional
//    public AnswerResponseDto createAnswer(AnswerRequestDto answerRequestDto) {
//        String originalFileName = file.getOriginalFilename();
//        File upload = new File(upladFolder, datePath);
//        upload.mkdirs()
//
//        AnswerEntity answerEntity1 = new AnswerEntity(answerRequestDto.getContent());
//        AnswerEntity saved = AnswerRepository.save(answerEntity1);
//        return new AnswerResponseDto(saved.getId(), saved.getContent(), answerEntity1.getLikes());
//    }


    @Transactional
    public AnswerResponseDto updateAnswer(Long answerId, AnswerRequestDto answerRequestDto) {
        AnswerEntity answerEntitySaved = AnswerRepository.findById(answerId).orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        answerEntitySaved.update(answerRequestDto.getContent());
        AnswerRepository.save(answerEntitySaved);
        return new AnswerResponseDto(answerEntitySaved.getContent());
    }

    @Transactional
    public String deleteAnswer(Long answerId) {
        AnswerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 답변입니다."));
        AnswerRepository.deleteById(answerId);
        return "삭제 완료";
    }


    // 내 답변 상세 조회
    @Transactional(readOnly = true)
    public AnswerResponseDto getAnswer(Long answerId) {
        AnswerEntity answerEntity = AnswerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 답변입니다."));
        return new AnswerResponseDto(answerEntity.getId(), answerEntity.getContent(), answerEntity.getLikes());
    }

    // 내 답변 리스트 조회
//    public List<AnswerResponseDto> getListAnswer() {
//        List<AnswerEntity> allAnswer = AnswerRepository.findAll();
//        List<AnswerResponseDto> answerResponseDtoList = new ArrayList<>();
//
//        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
//        List<CommentEntity> allComment = commentRepository.findAll();
//
//        for (CommentEntity commentEntity : allComment) {
//            commentResponseDtoList.add(new CommentResponseDto(commentEntity.getId(), commentEntity.getContent()));
//        }
//
//        for (AnswerEntity answerEntity : allAnswer) {
//            AnswerResponseDto responseDto = AnswerResponseDto.builder()
//                    .id(answerEntity.getAnswerId())
//                    .content(answerEntity.getContent())
//                    .answerLike(answerEntity.getLikes())
//                    .commentResponseDtoList(commentResponseDtoList)
//                    .build();
//            answerResponseDtoList.add(responseDto);
//        }
//        return answerResponseDtoList;
//
//    }
    @Transactional(readOnly = true)
    public CommentResultDto getListAnswer(Pageable pageable, int page) {
        List<AnswerResponseDto> resultList = new ArrayList<>();
        Page<AnswerEntity> answers = AnswerRepository.findAll(pageable.withPage(page - 1));
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        List<CommentEntity> allComment = commentRepository.findAll();
        for (CommentEntity commentEntity : allComment) {
            commentResponseDtoList.add(new CommentResponseDto(commentEntity.getId(), commentEntity.getContent()));
        }

        for (AnswerEntity answer1 : answers) {
            AnswerResponseDto answerResponseDto =
                    new AnswerResponseDto(answer1.getId(), answer1.getContent(), answer1.getLikes(), commentResponseDtoList);
            resultList.add(answerResponseDto);
        }

        CommentResultDto resultDto = new CommentResultDto(page, resultList);
        return resultDto;

    }
}

