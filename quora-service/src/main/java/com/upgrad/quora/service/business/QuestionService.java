package com.upgrad.quora.service.business;

import com.upgrad.quora.service.entity.QuestionEntity;

public interface QuestionService {
    QuestionEntity getQuestionByQuestionId(String questionId);
}
