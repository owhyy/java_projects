package com.example.emailadministration;

import javafx.util.Pair;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Properties;

public class UserDatabaseConnection {
    public Jdbi jdbi;

    public Jdbi getJdbi() {
        jdbi = Jdbi.create("jdbc:mysql://localhost:3306/email_app_db", "root", "Topimpabutterfly1");
        return jdbi;
    }
}

