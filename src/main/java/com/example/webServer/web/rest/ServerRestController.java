package com.example.webServer.web.rest;

import com.example.webServer.ServerProcess;
import com.example.webServer.data.entities.ServerEntity;
import com.example.webServer.data.repositories.Accounts;
import com.example.webServer.services.ServerService;
import com.example.webServer.web.errors.BadRequestException;
import com.example.webServer.web.models.Server;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(
        value = "/api",
        method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST}
)
public class ServerRestController {
    private final HashMap<String, Process> runningProcesses = new HashMap<>();
    private final ServerService serverService;
    private final Accounts accounts = new Accounts();

    public ServerRestController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping("/servers")
    public List<Server> getAll(@RequestParam(name="serverName", required = false)String serverName){
        return this.serverService.getAlLServers(serverName);
    }

    @GetMapping("/servers/getUserServers")
    public List<Server> getUserServers(@RequestBody String json){
        String username = json.replaceAll("\"", "");
        return this.serverService.getUserServers(username);
    }

    @GetMapping("/servers/sendServerStatusUpdate")
    public String updateServerStatus(@RequestBody String json){
        System.out.println("testing API" + json);
        String[] statusInformation = parseLoginJson(json);

        String type = statusInformation[0].replaceAll("\"", "");
        String serverName = statusInformation[1].replaceAll("\"", "");
        ServerEntity serverEntity = serverService.getServerByName(serverName);

        ServerProcess sp = new ServerProcess(serverEntity.getServerLocation());

        if(type.equals("Start Server")){
            runningProcesses.put(serverName, sp.startServer());

            return "{\"status\": \"" + "STARTING" + "\"}";
        } else if (type.equals("Stop Server")){
            sp.stopServer(runningProcesses.get(serverName));
            runningProcesses.remove(serverName);
            return "{\"status\": \"" + "STOPPING" + "\"}";
        } else {
            throw new BadRequestException("Unknown command to run");
        }
    }

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
