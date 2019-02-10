package com.upgrad.quora.service.business;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Service;


public interface QuestionService {
    QuestionEntity getQuestionByQuestionId(String questionId);
}
