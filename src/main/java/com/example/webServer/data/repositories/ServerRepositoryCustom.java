package com.example.webServer.data.repositories;

import com.example.webServer.data.entities.ServerEntity;

import java.util.List;

public interface ServerRepositoryCustom {

    List<ServerEntity> findAllServers();

    List<ServerEntity> findAllUserServers(String username);

}
