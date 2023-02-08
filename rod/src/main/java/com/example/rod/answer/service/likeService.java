package com.example.rod.answer.service;

import com.example.rod.answer.entity.AnswerEntity;
import com.example.rod.answer.entity.AnswerLike;
import com.example.rod.answer.repository.AnswerLikeRepository;
import com.example.rod.answer.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class likeService {

    private final AnswerRepository AnswerRepository;
    private final AnswerLikeRepository answerLikeRepository;

    // 좋아요 유무 체크
    @Transactional
    public boolean checkLike(Long answerId) {
        return answerLikeRepository.existsById(answerId);
    }

    @Transactional
    public void likeAnswer(Long answerId) {
        AnswerEntity answer = AnswerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("찾는 답변이 없습니다."));

        // 좋아요 데이터가 없을 때
        if (!checkLike(answerId)) {
            answerLikeRepository.save(new AnswerLike(answer));
            answer.setLikes(answerLikeRepository.countLikeAnswerById(answer.getId()));

        // 좋아요 데이터가 있을 때
        } else {
            answerLikeRepository.deleteById(answerId);
            answer.setLikes(answerLikeRepository.countLikeAnswerById(answer.getId()));

        }
    }
}
