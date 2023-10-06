package com.example.webServer.web.rest;

import com.example.webServer.data.repositories.Accounts;
import com.example.webServer.web.errors.BadRequestException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(
        value = "/api/login",
        method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST}
)
public class LoginRestController {
    private Accounts accounts = new Accounts();

    // setting default test username/password
    @GetMapping
    @CrossOrigin()
    public String getLoginToken(@RequestBody String jsonLoginInformation){
        System.out.println("JSON " + jsonLoginInformation);

        String[] loginInformation = parseLoginJson(jsonLoginInformation);

        String username = loginInformation[0];
        String password = loginInformation[1];

        System.out.println("username: " + username);
        System.out.println("password: " + password);

        if (!accounts.isValidAccount(username, password)){
            throw new BadRequestException("invalid login information");
        } else {
            // TODO give an actual token
            return "token: aValidToken";
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

