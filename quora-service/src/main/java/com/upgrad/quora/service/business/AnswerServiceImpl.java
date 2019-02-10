package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService{

    @Autowired
    AnswerDao answerDao;

    @Autowired
    UserDao userDao;

    @Autowired
    QuestionDao questionDao;

    private UserAuthEntity checkIfUserExitsByAccessToken(String accessToken) throws AuthorizationFailedException {
        // Check for user sign in ...
        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(accessToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        return userAuthEntity;
    }

    private AnswerEntity getAnswerEntity(String answerId) throws AnswerNotFoundException {
        // Check for answer UUID ...
        final AnswerEntity answerEntity = answerDao.getAnswerByAnswerId(answerId);
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        return answerEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public AnswerEntity createAnswer(final AnswerEntity answerEntity) {
        return answerDao.createAnswer(answerEntity);
    }

    @Override
    public UserEntity getUser(final String accessToken) throws AuthorizationFailedException {
        // Check for user sign in ...
        UserAuthEntity userAuthEntity = checkIfUserExitsByAccessToken(accessToken);
        // Check if user logout or not ...t()
        ZonedDateTime userLogoutTime = userAuthEntity.getLogout_at();
        if (userLogoutTime == null) {

            final UserEntity userEntity = userAuthEntity.getUser_id();
            return userEntity;
        }
        throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswerContent(final String accessToken, final String answerId, final String content) throws AuthorizationFailedException, AnswerNotFoundException {
        // Check for user sign in ...
        UserAuthEntity userAuthEntity = checkIfUserExitsByAccessToken(accessToken);
        // Check if user logout or not ...
        ZonedDateTime userLogoutTime = userAuthEntity.getLogout_at();
        if (userLogoutTime == null) {
            // Check for Answer exist in Database ....
            final AnswerEntity answerEntity = getAnswerEntity(answerId);
            // check of owner of answer .....
            if (answerEntity.getUser().getUser_id().equals(userAuthEntity.getUser_id().getUser_id())) {
                // update content in database ...
                answerDao.editAnswerContent(answerEntity);
                answerEntity.setAnswer(content);
                return answerEntity;
            }
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }
        throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(final String accessToken, final String answerId) throws AuthorizationFailedException, AnswerNotFoundException {
        // Check for user sign in ...
        UserAuthEntity userAuthEntity = checkIfUserExitsByAccessToken(accessToken);
        // Check if user logout or not ...
        ZonedDateTime userLogoutTime = userAuthEntity.getLogout_at();
        if (userLogoutTime == null) {
            // Check for Answer exist in Database ....
            final AnswerEntity answerEntity = getAnswerEntity(answerId);
            // check of owner of answer .....
            if (answerEntity.getUser().getUser_id().equals(userAuthEntity.getUser_id().getUser_id())) {
                // delete answer in database ...
                answerDao.deleteAnswer(answerEntity);
                return answerEntity;
            }
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }
        throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete an answer");
    }


    @Override
    public List<AnswerEntity> getAllAnswersToQuestion(final String accessToken, final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        // Check for user sign in ...
        UserAuthEntity userAuthEntity = checkIfUserExitsByAccessToken(accessToken);
        // Check if user logout or not ...
        ZonedDateTime userLogoutTime = userAuthEntity.getLogout_at();
        if (userLogoutTime == null) {
            final QuestionEntity questionEntity = questionDao.getQuestionByQuestionId(questionId);
            if (questionEntity == null) {
                throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
            }
            return answerDao.getAllAnswerByQuestionID(questionEntity);
        }
        throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get the answers");
    }

}
