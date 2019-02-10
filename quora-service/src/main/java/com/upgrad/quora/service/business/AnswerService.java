package com.upgrad.quora.service.business;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;

import java.util.List;

public interface AnswerService {
    AnswerEntity createAnswer(final AnswerEntity answerEntity) throws InvalidQuestionException;

    UserEntity getUser(final String accessToken) throws AuthorizationFailedException;

    AnswerEntity editAnswerContent(final String accessToken, final String answerId, final String content) throws AuthorizationFailedException, AnswerNotFoundException;

    AnswerEntity deleteAnswer(final String accessToken, final String answerId) throws AuthorizationFailedException,
            AnswerNotFoundException;

    List<AnswerEntity> getAllAnswersToQuestion(final String accessToken, final String questionId) throws AuthorizationFailedException, InvalidQuestionException;
}
