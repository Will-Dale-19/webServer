package com.example.webServer.data.repositories;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    public boolean isValidAccount(String username, String password) {
        return records.containsKey(username) && records.get(username).equals(password);
    }

}
