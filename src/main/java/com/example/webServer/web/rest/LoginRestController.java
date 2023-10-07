package com.example.webServer.web.rest;

import com.example.webServer.data.repositories.Accounts;
import com.example.webServer.web.errors.BadRequestException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(
        value = "/api",
        method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST}
)
public class LoginRestController {
    private final Accounts accounts = new Accounts();

    // setting default test username/password
    @GetMapping("/login")
    @CrossOrigin()
    public String getLoginToken(@RequestBody String jsonLoginInformation){
        String[] loginInformation = parseLoginJson(jsonLoginInformation);

        String username = loginInformation[0].replaceAll("\"", "");
        String password = loginInformation[1].replaceAll("\"", "");

        if (!accounts.isValidAccount(username, password)){
            throw new BadRequestException("invalid login information");
        } else {
            // TODO give an actual token
            return "{\"token\": \"" +username+"\"}";
        }
    }

    @GetMapping("/createAccount")
    @CrossOrigin()
    public String createNewAccount(@RequestBody String json){
        String[] accountInformation = parseLoginJson(json);
        String username = accountInformation[0].replaceAll("\"", "");
        String password = accountInformation[1].replaceAll("\"", "");
        System.out.println(json);

        if(accounts.accountExists(username)){
            throw new BadRequestException("account already exists");
        } else {
            if (accounts.createAccount(username, password)) {
                return "{\"token\": \"" +username+"\"}";
            } else {
                return "{\"accountCreationStatus\": \"failed\"}";
            }
        }

    }

    private String[] parseLoginJson(String jsonObj){
        String[] loginInformation = jsonObj.split(",");
        String[] returnArray = new String[2];
        returnArray[0] = loginInformation[0].split(":")[1];
        returnArray[1] = loginInformation[1].split(":")[1].replaceAll("}", "");
        return returnArray;
    }


}

