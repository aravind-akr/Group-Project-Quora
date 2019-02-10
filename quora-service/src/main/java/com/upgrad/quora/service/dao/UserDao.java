package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestionEntity(QuestionEntity questionEntity){

        entityManager.persist(questionEntity);
        return questionEntity;
    }


    public UserEntity getUser(final String userUuid){
        try{
            return entityManager.createNamedQuery("userByUuid",UserEntity.class).setParameter("uuid",userUuid).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    public UserEntity getUserByEmail(final String email){
        try{
            return entityManager.createNamedQuery("userByEmail",UserEntity.class).setParameter("email",email).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    public UserEntity getUserByUuid(final String uuid){
        try{
            return entityManager.createNamedQuery("userByUuid",UserEntity.class).setParameter("uuid",uuid).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    public UserAuthEntity createAuthToken(final UserAuthEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    public void updateUser(final UserEntity userEntity){
        entityManager.merge(userEntity);
    }

    public UserAuthEntity getUserAuthToken(final String accessToken){
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken",UserAuthEntity.class).setParameter("access_token",accessToken).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    public String deleteUser(final UserEntity userEntity){
        String uuid=userEntity.getUuid();
        entityManager.remove(userEntity);
        return uuid;

    }
    public UserAuthEntity getUserAuthTokenByUuid(final String uuid){
        try {
            return entityManager.createNamedQuery("userAuthTokenByUuid",UserAuthEntity.class).setParameter("uuid",uuid).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }


}
