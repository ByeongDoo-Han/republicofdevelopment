package com.example.rod.answer.repository;

import com.example.rod.answer.entity.AnswerLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerLikeRepository extends JpaRepository<AnswerLike, Long> {


    void deleteById(Long id);

    boolean existsById(Long id);

    int countLikeAnswerById(Long id);

}
