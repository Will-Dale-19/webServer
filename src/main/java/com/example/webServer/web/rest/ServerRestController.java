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
import java.util.Optional;

@RestController
@RequestMapping(
        value = "/api",
        method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST}
)
public class ServerRestController {
    private final HashMap<String, ServerProcess> runningProcesses = new HashMap<>();
    private final ServerService serverService;
    private final Accounts accounts = new Accounts();

    public ServerRestController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping("/servers")
    public List<Server> getAll(@RequestParam(name="serverName", required = false)String serverName){
        List<Server> servers = this.serverService.getAlLServers(serverName);
        for (Server server : servers){
            server.setServerStatus(runningProcesses.containsKey(server.serverName));
        }

        return servers;
    }

    @GetMapping("/servers/getUserServers")
    public List<Server> getUserServers(@RequestBody String json){
        String username = json.replaceAll("\"", "");
        List<Server> servers = this.serverService.getUserServers(username);
        for (Server server : servers){
            server.setServerStatus(runningProcesses.containsKey(server.serverName));
        }

        return servers;
    }


    @GetMapping("/servers/getServerStatus/{serverName}")
    public String getServerStatus(@PathVariable(name = "serverName") String serverName){
        serverName = serverName.replaceAll("\"", "");

        System.out.println("Getting status of: " + serverName + " it is: " + runningProcesses.containsKey(serverName));
        return "{\"serverStatus\": \"" +runningProcesses.containsKey(serverName)+"\"}";

    }

    @PostMapping("/servers/startServer")
    public String startServer(@RequestBody String serverName){
        serverName = serverName.replaceAll("\"", "");
        System.out.println("Starting: " + serverName);

        ServerEntity serverEntity = serverService.getServerByName(serverName.replaceAll("\"", ""));

        ServerProcess sp = new ServerProcess(serverEntity.getServerLocation());
        if (serverEntity.getServerLocation() == null) {
            System.out.println("Missing server location for: " + serverName);
            return "{\"status\": \"" + "FAILED-TO-START" + "\"}";
        }
        try {
            sp.startServer();
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Server start batch file is missing for: " + serverName);
            return "{\"status\": \"" + "FAILED-TO-START" + "\"}";
        }
        try {
            Optional<ProcessHandle> processHandle = ProcessHandle.of(sp.getProcess().pid());
            if (processHandle.isPresent() && processHandle.get().isAlive()) {
                runningProcesses.put(serverName, sp);
                return "{\"status\": \"" + "STARTING" + "\"}";
            } else {
                return "{\"status\": \"" + "FAILED-TO-START" + "\"}";
            }
        } catch (NullPointerException e){
            System.out.println("Server was unable to start: " + serverName);
            e.printStackTrace();
            return "{\"status\": \"" + "FAILED-TO-START" + "\"}";
        }
    }

    @PostMapping("/servers/stopServer")
    public String stopServer(@RequestBody String serverName){
        serverName = serverName.replaceAll("\"", "");
        System.out.println("Stopping: " + serverName);

        ServerProcess serverProcess = runningProcesses.get(serverName);
        // more robust handling of the scenarios is needed.
        if (serverProcess != null) {
            serverProcess.stopServer(serverProcess.getProcess());
            runningProcesses.remove(serverName);
            return "{\"status\": \"" + "STOPPING" + "\"}";
        } else {
            return "{\"status\": \"" + "FAILED-TO-STOP" + "\"}";
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
