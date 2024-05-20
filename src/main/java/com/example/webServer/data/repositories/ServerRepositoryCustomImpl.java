package com.example.webServer.data.repositories;

import com.example.webServer.data.entities.ServerEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerRepositoryCustomImpl implements ServerRepositoryCustom {

    /**
     * CRUD override to get all servers.
     * @return A list of all server entities that are stored at the given location.
     */
    @Override
    public List<ServerEntity> findAllServers() {
        // TODO Change from test data
        File serverLocation = new File("C:\\servers");
        File[] servers = serverLocation.listFiles();
        List<ServerEntity> entities = new ArrayList<>();
        for(File file : servers){
            try {
                ServerEntity entity = new ServerEntity();
                entity.setId(Long.parseLong(getServerInfo(file).get(1).get(0)));
                entity.setServerName(file.getName());
                entity.setServerLocation(file.getAbsolutePath());
                entities.add(entity);
            } catch (IllegalStateException e){
                // TODO properly catch
            }
        }
        return entities;
    }

    @Override
    public List<ServerEntity> findAllUserServers(String username){
        File serverLocation = new File("C:\\servers");
        File[] servers = serverLocation.listFiles();
        List<ServerEntity> entities = new ArrayList<>();
        for(File file : servers){
            try {
                if (username.equals(getServerInfo(file).get(1).get(1))) {
                    ServerEntity entity = createServerEntity(file);
                    entities.add(entity);
                }
            } catch (IllegalStateException e){
                System.out.println(file + " has no information file!");
            }
        }
        return entities;
    }

    @Override
    public ServerEntity getServerByName(String serverName){
        File serverLocation = new File("C:\\servers");
        File[] servers = serverLocation.listFiles();
        for(File file : servers){
            if(file.getName().equals(serverName)) {
                return createServerEntity(file);
            }
        }
        return null;
    }

    /**
     * Creates a new server entity from the given file
     * @param file the file/directory of the server
     * @return A new server entity.
     */
    private ServerEntity createServerEntity(File file){
        ServerEntity entity = new ServerEntity();
        entity.setId(Long.parseLong(getServerInfo(file).get(1).get(0)));
        entity.setOwner(getServerInfo(file).get(1).get(1));
        entity.setServerName(file.getName());
        entity.setServerLocation(file.getAbsolutePath());
        return entity;
    }

    /**
     * Function to get the server ID from the server files.
     * @param file The server directory.
     * @return The ID of the server.
     */
    private List<List<String>> getServerInfo(File file) {
        File[] files = file.listFiles();
        List<List<String>> records = new ArrayList<>();

        for (File file1 : files) {
            String fileName = file1.getName();

            if (fileName.equals("serverInfo.csv")) {

                try (BufferedReader br = new BufferedReader(new FileReader(file1.getAbsolutePath()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(",");
                        records.add(Arrays.asList(values));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e); // TODO fix with better error
                }
            }
        }
        if (records.size() < 1){
            throw new IllegalStateException("Server does not have an information file.");
        }
        return records;
    }
}
