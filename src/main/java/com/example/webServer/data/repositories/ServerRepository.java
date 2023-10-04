package com.example.webServer.data.repositories;

import com.example.webServer.data.entities.ServerEntity;
import org.springframework.data.repository.CrudRepository;

public interface ServerRepository extends CrudRepository<ServerEntity, Long>, ServerRepositoryCustom {

    ServerEntity findByServerName(String name);

}
