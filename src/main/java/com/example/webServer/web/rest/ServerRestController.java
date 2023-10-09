package com.example.webServer.web.rest;

import com.example.webServer.services.ServerService;
import com.example.webServer.web.errors.BadRequestException;
import com.example.webServer.web.models.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servers")
public class ServerRestController {
    private final ServerService serverService;

    public ServerRestController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping
    public List<Server> getAll(@RequestParam(name="serverName", required = false)String serverName){
        return this.serverService.getAlLServers(serverName);
    }

    @GetMapping("/getUserServers")
    public List<Server> getUserServers(@RequestBody String json){
        String[] accountInformation = parseLoginJson(json);
        String username = accountInformation[0].replaceAll("\"", "");
        return this.serverService.getUserServers(username);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Server createServer(@RequestBody Server server){
        return this.serverService.createOrUpdate(server);
    }

    @GetMapping("/{id}")
    public Server getServer(@PathVariable("id")long id){
        return this.serverService.getServer(id);
    }

    @PutMapping("/{id}")
    public Server updateServer(@PathVariable("id")long id, @RequestBody Server server){
        if(id != server.getServerId()){
            throw new BadRequestException("ids do not match");
        }
        return this.serverService.createOrUpdate(server);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteServer(@PathVariable("id")long id){
        this.serverService.deleteServer(id);
    }

    private String[] parseLoginJson(String jsonObj){
        String[] loginInformation = jsonObj.split(",");
        String[] returnArray = new String[2];
        returnArray[0] = loginInformation[0].split(":")[1];
        returnArray[1] = loginInformation[1].split(":")[1].replaceAll("}", "");
        return returnArray;
    }


}
