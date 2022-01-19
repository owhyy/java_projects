package com.company;


/* Command line interface class.
 The command line has options for:
1. Checking the balance
2. Making a deposit
3. Making a withdrawal
4. Viewing the previous transaction
5. Calculating Interest
6. Exiting the application
*/

import jdk.dynalink.NamedOperation;

import java.util.Scanner;

public class Interface {
    // line characters for visuals
    private final static String FULLLINESTRING = "==============================="; // 31
    private final static String ONETHIRDLINESTRING = "===================="; // 20
    private final static String HALFLINESTRING = "==============="; // 15

    // main account, initially empty
    private Account account = new Account(0);

    // method that displays the main menu
    private void displayMainMenu() {
        //System.out.println(FULLLINESTRING + ONETHIRDLINESTRING);
        //System.out.println(HALFLINESTRING  + " Welcome to the bank " + HALFLINESTRING);
        System.out.println("How can we help you today?");
        System.out.println("\t1. Check the balance");
        System.out.println("\t2. Make a deposit");
        System.out.println("\t3. Make a withdrawal");
        System.out.println("\t4. View transaction history");
        System.out.println("\t5. Calculate interest");
        System.out.println("\t6. Exit");
        System.out.println("\nPlease enter an option below");
        System.out.print("> ");
        //System.out.println(FULLLINESTRING+ONETHIRDLINESTRING);
    }

    // method that reads the input and handles it
    private void handleInput() {
        int choice=0;
        boolean running = true;
        Scanner input = new Scanner(System.in);
        while (running) {
            choice = input.nextInt();
            switch (choice) {
                case 1:
                    handleCheckBalance();
                    displayMainMenu();
                    break;
                case 2:
                    handleMakeDeposit();
                    displayMainMenu();
                    break;
                case 3:
                    handleMakeWithdrawal();
                    displayMainMenu();
                    break;
                case 4:
                    handleViewHistory();
                    displayMainMenu();
                    break;
                case 5:
                    handleCalculateInterest();
                    displayMainMenu();
                    break;
                case 6:
                    handleExit();
                    running=false;
                    break;
                default:
                    displayMainMenu();
                    break;
            }
        }
    }

    // maybe make a function that will caption automatically?

    // handles the option "Check the balance"
    private void handleCheckBalance() {
        System.out.println("Current balance: " + account.getBalance());
    }
    // handles the option "Make a deposit"
    private void handleMakeDeposit() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the sum you wish to deposit: ");
        System.out.print("> ");
        double deposit = input.nextDouble();
        account.deposit(deposit);
        account.logAction("+" + deposit);
        System.out.println("Deposit successful!");
    }
    // handles the option "Make a withdrawal"
    private void handleMakeWithdrawal() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the sum you wish to withdraw: ");
        System.out.print("> ");
        double withdraw = input.nextDouble();
        if (account.canWithdraw(withdraw))
        {
            account.withdraw(withdraw);
            System.out.println("Withdrawal successful!");
            account.logAction("-" + withdraw);
        } else {
            System.out.println("Sorry, but you cannot withdraw " + withdraw + "; you only have " + account.getBalance() + " in your account");
        }
    }
    // handles the option "View transaction history"
    private void handleViewHistory() {
        System.out.println("Transaction history: ");
        account.displayHistory();
    }
    // handles the option "Calculate interest"
    private void handleCalculateInterest() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the rate: (1-100%)");
        System.out.print("> ");
        double rate = input.nextDouble() / 100;
        System.out.println("Enter the period: ");
        int period = input.nextInt();
        System.out.print("> ");
        System.out.println("\nDepositing " + account.getBalance() + " for a period of " + period + " at a rate of " + rate*100 + "% will total to " +account.getInterest(rate, period));
    }
    // handles the option "Exit"
    private void handleExit() {
        System.out.println("Have a nice day!");
    }

    // function that executes the interface
    public void run() {
        displayMainMenu();
        handleInput();
    }

}
