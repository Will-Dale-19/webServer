package com.example.webServer.data.repositories;

import java.io.*;
import java.util.HashMap;

public class Accounts {

    private final HashMap<String, String> records = new HashMap<>();

    /**
     * Initialization of the user accounts into a HashMap.
     */
    public Accounts(){

        File accountsLocation = new File("testData/accounts/accountInfo.csv");

        try (BufferedReader br = new BufferedReader(new FileReader(accountsLocation.getAbsolutePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.put(values[0], values[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO fix with better error
        }

    }

    /**
     * Function to see if an account with the given username already exists.
     * @param username the username to check
     * @return whether the account exists.
     */
    public boolean accountExists(String username){
        return records.containsKey(username);
    }

    /**
     * Function to create a new account.
     * @param username the ideal username for the account
     * @param password the ideal password for the account
     * @return whether the account creation was successful.
     */
    public boolean createAccount(String username, String password){
        if(!records.containsKey(username)) {
            records.put(username, password);
            writeToAccountFile(username, password);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Function to physically write the data to the database.
     * @param username the accounts username
     * @param password the accounts password
     */
    private void writeToAccountFile(String username, String password){
        try(FileWriter fw = new FileWriter("testData/accounts/accountInfo.csv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(username + "," + password);

        } catch (IOException e) {
            System.out.println("failed to write to account file");
        }

    }

    /**
     * Function to see if the given credentials can log into an account.
     * @param username username of the account
     * @param password password of the account
     * @return whether the login was successful or not.
     */
    public boolean isValidAccount(String username, String password) {
        return records.containsKey(username) && records.get(username).equals(password);
    }


}
