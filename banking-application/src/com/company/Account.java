package com.company;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private double balance;
    private List<String> actionList;

    Account(double balance) {
        balance = balance;
        actionList = new ArrayList<>();
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void logAction(String action) {
        actionList.add(action);
    }

    public void displayHistory() {
        for(String s : actionList) {
            System.out.println(s);
        }
    }

    public void deposit(double deposit) {
        this.balance += deposit;
    }

    public boolean canWithdraw(double amount) {
        return this.balance - amount >= 0;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public double getInterest(double rate, int period) {
        double interest = balance;
        for (int i = 1; i <= period; ++i) {
            interest += interest * rate;
        }
        return interest;
    }
}
