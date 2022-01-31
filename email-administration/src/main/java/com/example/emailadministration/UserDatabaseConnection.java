package com.example.emailadministration;

import org.jdbi.v3.core.Jdbi;

public class UserDatabaseConnection {
    public Jdbi jdbi;

    public Jdbi getJdbi() {
        jdbi = Jdbi.create("jdbc:mysql://localhost:3306/email_app_db", "root", "Topimpabutterfly1");
        return jdbi;
    }
}

