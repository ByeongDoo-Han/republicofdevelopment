package com.example.rod.answer.entity;

import com.example.rod.comment.entity.CommentEntity;
import com.example.rod.share.TimeStamped;
import com.example.rod.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerEntity extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String content;
    @Column
    private int likes;

    private String originalFileName;
    private String savedFileName;

    @OneToMany(mappedBy = "answerEntity", cascade = CascadeType.ALL)
    @OrderBy("createdAt DESC")
    private List<CommentEntity> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

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
