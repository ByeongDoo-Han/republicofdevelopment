package com.example.rod.answer.entity;

import com.example.rod.comment.entity.commentEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;
    private String content;
    private int likes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    @JsonIgnore
    private commentEntity comment;


    public AnswerEntity(String content) {
        this.content = content;
    }

    public void update(String content){
        this.content= content;
    }

    public void setLikes(int likes){
        this.likes = likes;
    }

}
