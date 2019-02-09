package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.dao.AnswerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnswerController {


    @Autowired
    private AnswerDao dao;

    @RequestMapping(value ="/question/{questionId}/answer/create", method =RequestMethod.POST)
    public ResponseEntity<AnswerResponse> createAnswer(@RequestBody AnswerRequest answer)
    {

return null;
    }

    @RequestMapping(value ="/answer/edit/{answerId}", method =RequestMethod.PUT )
    public ResponseEntity<AnswerEditResponse> editAnswerContent (@RequestBody AnswerEditRequest request)
    {
        return null;
    }


    @RequestMapping(value ="/answer/delete/{answerId}", method =RequestMethod.DELETE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer ()
    {
        return null;
    }

    @RequestMapping(value ="/answer/all/{questionId}", method =RequestMethod.GET)
    public ResponseEntity<AnswerDetailsResponse> getAllAnswersToQuestion()
    {
        return null;
    }
}
