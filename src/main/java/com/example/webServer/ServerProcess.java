package com.example.webServer;

import lombok.Getter;
import lombok.Setter;

import java.io.*;

public class ServerProcess {
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
            System.out.println("PID: " + p.pid());
            assert pb.redirectOutput().file() == log;
            //p.waitFor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }/* catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
    */
    }

    /**
     * Stop the server, ideally with the /stop command to ensure a safe shutdown.
     */
    public void stopServer(Process p){
        System.out.println("stopping server...");

        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()))) {

            writer.write("/stop");

            writer.flush();

            p.destroy();
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

}
