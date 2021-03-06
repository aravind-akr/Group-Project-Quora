package com.upgrad.quora.service.business;

import org.springframework.stereotype.Service;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
@Service
public class DeleteQuestionBusinessService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity verifyAuthToken(final String accessToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (((UserAuthEntity) userAuthTokenEntity).getLogout_at() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a question");
        } else {
            return userDao.getUserByUuid(userAuthTokenEntity.getUuid());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity verifyQUuid(final String questionUuid)throws InvalidQuestionException {
        QuestionEntity questionEntity = questionDao.getQuestionByQUuid(questionUuid);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        } else {
            return questionEntity;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteQuestion(final QuestionEntity questionEntityToDelete, final UserEntity signedinUserEntity ) throws AuthorizationFailedException {

        if (signedinUserEntity.getRole().equalsIgnoreCase("admin")||(questionEntityToDelete.getUser_id().getUsername()==signedinUserEntity.getUsername())) {

            return questionDao.deleteQuestion(questionEntityToDelete);
        }
        else{
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }
    }
}
