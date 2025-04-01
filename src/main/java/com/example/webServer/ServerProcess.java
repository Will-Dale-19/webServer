package com.example.webServer;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ServerProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerProcess.class);

    private final String serverLocation;

    @Getter
    @Setter
    private Process process;

    public ServerProcess(String serverLocation){
        this.serverLocation = serverLocation;
    }

    /**
     * Starts the server
     */
    public void startServer() throws RuntimeException {

        ProcessBuilder pb = new ProcessBuilder(serverLocation + "\\start.bat");

        pb.directory(new File(serverLocation));
        File log = new File(serverLocation + "\\log");
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
        try {
            Process p = pb.start();
            this.process = p;
            LOGGER.info("Starting server with PID: {}", p.pid());
            assert pb.redirectOutput().file() == log;
            //p.waitFor();
        } catch (IOException e) {
            LOGGER.error("Failed to start server.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Stop the server, ideally with the /stop command to ensure a safe shutdown.
     */
    public void stopServer(Process p){
        LOGGER.info("Attempting to stop server with PID: {}", p.pid());
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()))) {

            writer.write("/stop");

            writer.flush();

            p.destroy();
        }  catch (IOException e) {
            LOGGER.error("Failed to stop server with PID: {}", p.pid());
            throw new RuntimeException(e);
        }



    }

}
