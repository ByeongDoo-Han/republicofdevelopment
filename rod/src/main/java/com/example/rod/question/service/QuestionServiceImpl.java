package com.example.rod.question.service;

import com.example.rod.answer.dto.AnswerWithCommentsDto;
import com.example.rod.answer.entity.Answer;
import com.example.rod.answer.repository.AnswerRepository;
import com.example.rod.comment.dto.CommentResponseDto;
import com.example.rod.comment.entity.Comment;
import com.example.rod.comment.repository.CommentRepository;
import com.example.rod.question.dto.*;
import com.example.rod.question.entity.Question;
import com.example.rod.question.repository.QuestionRepository;
import com.example.rod.security.details.UserDetailsImpl;
import com.example.rod.user.entity.User;
import com.example.rod.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import lombok.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

    private final QuestionHashTagService questionHashTagService;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final UserDetailsService userDetailsService;

    @Override
    @Transactional
    public void createQuestion(QuestionRequest questionRequest, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        String hashtagStrs = questionRequest.getHashtagStrs();

        Question question = Question.builder()
                .title(questionRequest.getTitle())
                .content(questionRequest.getContent())
                .user(user)
                .isClosed(false)
                .difficulty(0f) //  ?????? ????????? 0?????? ??????.
                .build();

        questionRepository.save(question);
        // ?????? ??????
        questionHashTagService.saveHashTags(question, hashtagStrs);
    }

    @Override
    @Transactional
    public GetQuestionsResponse getMyQuestions(UserDetailsImpl userDetails, Pageable pageable, int page) {

        User user = userDetails.getUser();

        Page<Question> questionList = questionRepository.findAllByUser(user, pageable.withPage(page - 1));

        List<QuestionResponse> questionResponseList = new ArrayList<>();

        for (Question question : questionList) {
            questionResponseList.add(QuestionResponse.builder()
                    .questionId(question.getId())
                    .title(question.getTitle())
                    .nickname(question.getUser().getName())
                    .answerCount(question.getAnswers().size())
                    .createdAt(question.getCreatedAt()).build());
        }
        return new GetQuestionsResponse(page, questionResponseList);
    }

    @Override
    @Transactional
    public GetQuestionsResponse getQuestions(Pageable pageable, int page) {

        Page<Question> questionList = questionRepository.findAll(pageable.withPage(page - 1));


        List<QuestionResponse> questionResponseList = new ArrayList<>();


        for (Question question : questionList) {
            questionResponseList.add(QuestionResponse.builder()
                    .questionId(question.getId())
                    .title(question.getTitle())
                    .nickname(question.getUser().getName())
                    .answerCount(question.getAnswers().size())
                    .createdAt(question.getCreatedAt()).build());
        }

        return new GetQuestionsResponse(page, questionResponseList);
    }


    @Override
    @Transactional
    public QuestionWithAnswersResponse getSpecificQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow
                (() -> new IllegalArgumentException("?????? ???????????? ????????? ????????????."));

        List<AnswerWithCommentsDto> answerWithComments = new ArrayList<>();


        // 1. AnswerList ????????? ???????????? ????????????.
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "createdAt"));

        Page<Answer> answerList = answerRepository.findByQuestion(question, pageRequest);

        // 2.Answer -> AnswerResponseDto??? ??????.

        for (Answer answer : answerList) {
            List<CommentResponseDto> comments = new ArrayList<>();
            for (Comment comment : answer.getComments()) {
                CommentResponseDto commentResponseDto = new CommentResponseDto(comment.getId(), comment.getContent());
                comments.add(commentResponseDto);
            }
            AnswerWithCommentsDto answerWithCommentsDto = new AnswerWithCommentsDto(answer.getId(), answer.getContent(), answer.getLikes(), comments);
            answerWithComments.add(answerWithCommentsDto);
        }

        // 3. Question ??? Tag??? ??????.
        HashTagDto hashTagDto = questionHashTagService.findTagsByQuestionId(questionId);

        QuestionWithAnswersResponse questionWithAnswersResponse = new QuestionWithAnswersResponse(question.getTitle(), question.getContent(), question.getDifficulty(), answerWithComments, hashTagDto);

        return questionWithAnswersResponse;
    }


    @Override
    @Transactional
    public void selectAnswerForQuestion(Long questionId, Long answerId, UserDetailsImpl userDetails) {

        User questioner = userDetails.getUser();

        Question question = questionRepository.findById(questionId).orElseThrow
                (() -> new IllegalArgumentException("?????? ???????????? ????????? ????????????."));

        Answer answer = answerRepository.findById(answerId).orElseThrow(
                () -> new IllegalArgumentException("???????????? ???????????? ????????? ????????????.")
        );

        question.processSelectionResult(questioner, question, answer);
    }


    @Override
    @Transactional
    public void changeQuestionTitle(Long questionId, PatchQuestionTitleRequest patchQuestionTitleRequest, UserDetailsImpl userDetails) {
        Question question = questionRepository.findById(questionId).orElseThrow
                (() -> new IllegalArgumentException("?????? ???????????? ????????? ????????????."));
        User user = userDetails.getUser();
        question.editTitle(user, patchQuestionTitleRequest.getTitle());
    }

    // ?????? ?????? ??????
    @Override
    @Transactional
    public void changeQuestionContent(Long questionId, PatchQuestionContentRequest patchQuestionContentRequest, UserDetailsImpl userDetails) {
        Question question = questionRepository.findById(questionId).orElseThrow
                (() -> new IllegalArgumentException("?????? ???????????? ????????? ????????????."));
        User user = userDetails.getUser();
        question.editContent(user, patchQuestionContentRequest.getContent());
    }


    //?????? ??????
    @Override
    @Transactional
    public void deleteQuestion(Long questionId, UserDetailsImpl userDetails) {


        Question question = questionRepository.findById(questionId).orElseThrow(
                () -> new IllegalArgumentException("?????? ???????????? ????????? ????????????.")
        );
        User user = userDetails.getUser();

        if (question.isOwnedBy(user)) {
            questionRepository.deleteById(questionId);
        } else {
            throw new IllegalArgumentException("?????? ????????? ?????? ???????????????.");
        }


    /*// ????????? ????????? ?????????
    @Value("${app.upload.dir:${user.home}}")
    private String uploadDir;
    public void uploadImage(MultipartFile image){
        Path copyOfLocation = Paths.get(uploadDir + File.separator +  StringUtils.cleanPath(image.getOriginalFilename()));
        try {
            // inputStream??? ????????????
            // copyOfLocation (????????????)??? ????????? ??????.
            // copy??? ????????? ????????? ???????????? REPLACE(????????????), ??????????????? ??????
            Files.copy(image.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file : " + image.getOriginalFilename());
        }
    }*/
    }
}
