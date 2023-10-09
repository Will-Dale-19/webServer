package com.example.webServer.services;

import com.example.webServer.data.entities.ServerEntity;
import com.example.webServer.data.repositories.ServerRepository;
import com.example.webServer.web.errors.NotFoundException;
import com.example.webServer.web.models.Server;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServerService {

    private final ServerRepository serverRepository;

    public ServerService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public List<Server> getAlLServers(String filterName){
        List<Server>servers = new ArrayList<>();
        if(StringUtils.hasLength(filterName)){
            ServerEntity entity = this.serverRepository.findByServerName(filterName);
            servers.add(this.translateDbToWeb(entity));
        } else {
            Iterable<ServerEntity> entities = this.serverRepository.findAllServers();
            entities.forEach(entity -> {
                servers.add(this.translateDbToWeb(entity));
            });
        }
        return servers;
    }

    public List<Server> getUserServers(String filterUser){
        List<Server>servers = new ArrayList<>();
        Iterable<ServerEntity> entities = this.serverRepository.findAllServers();
        entities.forEach(entity -> {
            servers.add(this.translateDbToWeb(entity));
        });

        return servers;
    }

    public Server getServer(long id){
        Optional<ServerEntity> optional = this.serverRepository.findById(id);
        if(optional.isEmpty()){
            throw new NotFoundException("server not found with id");
        }
        return this.translateDbToWeb(optional.get());
    }

    public Server createOrUpdate(Server server){
        ServerEntity entity = this.translateWebToDb(server);
        entity = this.serverRepository.save(entity);
        return this.translateDbToWeb(entity);
    }

    public void deleteServer(long id){
        this.serverRepository.deleteById(id);
    }

    private ServerEntity translateWebToDb(Server server){
        ServerEntity entity = new ServerEntity();
        entity.setId(server.getServerId());
        entity.setServerName(server.getServerName());
        entity.setServerLocation(server.getServerLocation());
        return entity;
    }

    private Server translateDbToWeb(ServerEntity entity){
        return new Server(entity.getId(), entity.getServerName(), entity.getServerLocation());
    }

}
