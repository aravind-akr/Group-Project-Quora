package com.upgrad.quora.service.business;

import org.springframework.stereotype.Service;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditQuestionContentBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity verifyUserStatus(final String questionId, final String accessToken) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthTokenEntity.getLogout_at() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
        }
        QuestionEntity existingQuestionEntity = questionDao.getQuestionByQUuid(questionId);
        if (existingQuestionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        if (existingQuestionEntity.getUser_id().getUsername().equalsIgnoreCase(userAuthTokenEntity.getUser_id().getUsername())) {
            return questionDao.updateQuestion(existingQuestionEntity);
        } else {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity updateQuestion(final QuestionEntity updatedQuestionEntity){
        return questionDao.updateQuestion(updatedQuestionEntity);
    }
}
