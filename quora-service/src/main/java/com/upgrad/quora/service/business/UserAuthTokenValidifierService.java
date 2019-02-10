package com.upgrad.quora.service.business;

import com.upgrad.quora.service.common.Identifier;
import org.springframework.stereotype.Service;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserAuthTokenValidifierService implements Identifier {


    @Autowired
    UserDao userDao;

    boolean userAuthTokenValidityCheck(String accessToken, String endpointIdentifier) throws AuthorizationFailedException {
        UserAuthEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);

        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else {
            String logoutAt = String.valueOf(userDao.getUserAuthToken(accessToken)
                    .getLogout_at());

            if (!logoutAt.equals("null")) {

                String errorMessage = null;
                switch(endpointIdentifier){

                    case GET_ALL_QUESTIONS:
                        errorMessage=GET_ALL_QUESTIONS;
                        break;

                    case CHECK_QUESTION:
                        errorMessage=CHECK_QUESTION;
                        break;

                    case DELETE_QUESTION:
                        errorMessage=DELETE_QUESTION;
                        break;

                    case GET_QUESTION_BY_USER:
                        errorMessage=GET_QUESTION_BY_USER;
                        break;

                    case CHECK_ANSWER:
                        errorMessage = CHECK_ANSWER;
                        break;

                    case DELETE_ANSWER:
                        errorMessage = DELETE_ANSWER;
                        break;

                    case GET_ALL_ANSWERS:
                        errorMessage = GET_ALL_ANSWERS;
                        break;


                }

                throw new AuthorizationFailedException("ATHR-002",
                        errorMessage);
            } else {
                return true;
            }
        }
    }
}
