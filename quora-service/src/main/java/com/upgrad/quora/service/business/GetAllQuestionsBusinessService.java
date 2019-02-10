package com.upgrad.quora.service.business;

import com.upgrad.quora.service.common.Identifier;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllQuestionsBusinessService implements Identifier {
    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    UserAuthTokenValidifierService userAuthTokenValidifierService;

    public List<QuestionEntity> getAllQuestions(String accessToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);

        List<QuestionEntity> questionEntityList= new ArrayList<>();

        if(userAuthTokenValidifierService.userAuthTokenValidityCheck(accessToken,GET_ALL_QUESTIONS)){
            questionEntityList= questionDao.getAllQuestions();
        }
        return questionEntityList;
    }
}
