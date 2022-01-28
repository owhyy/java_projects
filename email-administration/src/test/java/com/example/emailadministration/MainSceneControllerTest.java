package com.example.emailadministration;

import jdk.jfr.Label;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainSceneControllerTest {
    UserDatabaseConnection userDatabaseConnection;
    Jdbi jdbi;

    @BeforeEach
    void setUp() {
        userDatabaseConnection=new UserDatabaseConnection();
        jdbi=userDatabaseConnection.getJdbi();
    }

    //@Test
    @DisplayName("Should signal true if database connection is established by doing a simple select")
    public void testConnection() {
        List<String> names = jdbi.withHandle(handle ->
                handle.createQuery("SELECT FirstName FROM users")
                        .mapTo(String.class)
                        .list());
        assertFalse(names.isEmpty());
    }

    @Test
    @DisplayName("Should signal true, because account exists")
    public void validateLoginTest1() {

        jdbi.useHandle(handle ->
                handle.execute("INSERT INTO users(idUser, FirstName, LastName, DateOfBirth, Login, Password, EmailAddress, SecretQuestion, SecretQuestionAnswer)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", 0, "test", "test", "1990-01-01", "test", "test", "test", "test", "test"));
        // first call the create method, then search for it, then delete it from db
        String searchQuery =  "SELECT count(1) FROM users WHERE Login = 'test' AND Password = 'test'";

        int queryResult = jdbi.withHandle(handle ->
                handle.createQuery(searchQuery)
                        .mapTo(Integer.class)
                        .one());

       jdbi.useHandle(handle ->
               handle.execute("DELETE FROM users WHERE Login = 'test';"));

        assertEquals(1, queryResult);
    }
}