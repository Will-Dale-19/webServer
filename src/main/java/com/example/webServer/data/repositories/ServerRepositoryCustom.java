package com.example.webServer.data.repositories;

import com.example.webServer.data.entities.ServerEntity;

import java.util.List;

public interface ServerRepositoryCustom {

    public List<ServerEntity> findAllServers();

}
