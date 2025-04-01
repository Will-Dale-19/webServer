package com.example.webServer.web.rest;

import com.example.webServer.ServerProcess;
import com.example.webServer.data.entities.ServerEntity;
import com.example.webServer.data.repositories.Accounts;
import com.example.webServer.services.ServerService;
import com.example.webServer.web.errors.BadRequestException;
import com.example.webServer.web.models.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRestController.class);
    private final HashMap<String, ServerProcess> runningProcesses = new HashMap<>();
    private final ServerService serverService;
    private final Accounts accounts = new Accounts();

    public ServerRestController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping("/servers")
    public List<Server> getAll(@RequestParam(name="serverName", required = false)String serverName){
        List<Server> servers = this.serverService.getAlLServers(serverName);

        if (serverName != null) {
            LOGGER.info("An admin has requested a server: {}", serverName);
        } else {
            LOGGER.info("An admin has requested all servers");
        }

        for (Server server : servers){
            server.setServerStatus(runningProcesses.containsKey(server.serverName));
        }

        return servers;
    }

    @PostMapping("/servers/getUserServers")
    public List<Server> getUserServers(@RequestBody String json){
        String username = json.replaceAll("\"", "");

        LOGGER.info("{} is requesting their servers.", username);

        List<Server> servers = this.serverService.getUserServers(username);
        for (Server server : servers){
            server.setServerStatus(runningProcesses.containsKey(server.serverName));
        }

        return servers;
    }


    @GetMapping("/servers/getServerStatus/{serverName}")
    public String getServerStatus(@PathVariable(name = "serverName") String serverName){
        serverName = serverName.replaceAll("\"", "");

        LOGGER.info("Getting status of: {} it is: {}", serverName, runningProcesses.containsKey(serverName));

        return "{\"serverStatus\": \"" +runningProcesses.containsKey(serverName)+"\"}";
    }

    @PostMapping("/servers/startServer")
    public String startServer(@RequestBody String serverName){
        serverName = serverName.replaceAll("\"", "");

        LOGGER.info("Attempting to start server: {}", serverName);

        ServerEntity serverEntity = serverService.getServerByName(serverName.replaceAll("\"", ""));

        ServerProcess sp = new ServerProcess(serverEntity.getServerLocation());
        if (serverEntity.getServerLocation() == null) {
            LOGGER.error("Missing server location for: {}", serverName);
            return "{\"status\": \"" + "FAILED-TO-START" + "\"}";
        }
        try {
            sp.startServer();
        } catch (RuntimeException e) {
            e.printStackTrace();
            LOGGER.error("Server start batch file is missing for: {}", serverName);
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
            LOGGER.error("Server was unable to start: {}", serverName);
            e.printStackTrace();
            return "{\"status\": \"" + "FAILED-TO-START" + "\"}";
        }
    }

    @PostMapping("/servers/stopServer")
    public String stopServer(@RequestBody String serverName){
        serverName = serverName.replaceAll("\"", "");

        LOGGER.info("Attempting to stop server: {}", serverName);

        ServerProcess serverProcess = runningProcesses.get(serverName);
        // more robust handling of the scenarios is needed.
        if (serverProcess != null) {
            serverProcess.stopServer(serverProcess.getProcess());
            runningProcesses.remove(serverName);
            LOGGER.info("Server successfully stopped.");
            return "{\"status\": \"" + "STOPPING" + "\"}";
        } else {
            LOGGER.error("Failed to stop server.");
            return "{\"status\": \"" + "FAILED-TO-STOP" + "\"}";
        }
    }

    @PostMapping("/login")
    @CrossOrigin()
    public String getLoginToken(@RequestBody String jsonLoginInformation){
        String[] loginInformation = parseLoginJson(jsonLoginInformation);

        String username = loginInformation[0].replaceAll("\"", "");
        String password = loginInformation[1].replaceAll("\"", "");

        LOGGER.info("Attempting to log in user: {}", username);

        if (!accounts.isValidAccount(username, password)){
            LOGGER.info("Bad login attempt from: {}", username);
            throw new BadRequestException("invalid login information");
        } else {
            // TODO give an actual token
            LOGGER.info("User successfully authorized: {}", username);
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
