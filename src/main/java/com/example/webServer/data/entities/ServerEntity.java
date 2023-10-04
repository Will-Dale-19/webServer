package com.example.webServer.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
public class ServerEntity {

    @Id
    private long id;

    private String serverName;

    private String serverLocation;


}
