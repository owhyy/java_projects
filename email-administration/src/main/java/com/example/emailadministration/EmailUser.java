package com.example.emailadministration;

import java.util.Date;

public class EmailUser {
    private int idUser;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String login;
    private String password;
    private String emailAddress;
    private String secretQuestion;
    private String secretQuestionAnswer;

    EmailUser(String firstName, String lastName, Date dateOfBirth,
              String login, String password, String emailAddress,
              String secretQuestion, String secretQuestionAnswer) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.dateOfBirth=dateOfBirth;
        this.login=login;
        this.password=password;
        this.emailAddress=emailAddress;
        this.secretQuestion=secretQuestion;
        this.secretQuestionAnswer=secretQuestionAnswer;
    }

    public String getQueryFormat() {
        return "i like boobs";
    }
}
