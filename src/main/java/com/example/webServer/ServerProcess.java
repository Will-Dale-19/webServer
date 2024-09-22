package com.example.webServer;

import java.io.*;

public class ServerProcess {
    ProcessBuilder pb;
    String serverLocation;

    public ServerProcess(String serverLocation){
        this.serverLocation = serverLocation;
    }

    /**
     * Starts the server
     * @return returns the started server process instance.
     */
    public Process startServer(){
        pb = new ProcessBuilder(serverLocation + "\\start.bat");
        pb.directory(new File(serverLocation));
        File log = new File(serverLocation + "\\log");
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
        try {
            Process p = pb.start();
            System.out.println("PID: " + p.pid());
            assert pb.redirectOutput().file() == log;
            //p.waitFor();
            return p;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }/* catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        */
    }

    /**
     * Stop the server, ideally with the /stop command to ensure a safe shutdown.
     * @param p the process that is to be shutdown
     */
    public void stopServer(Process p){
        p.destroy();

        /*
        OutputStream stdin = p.getOutputStream();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

        try {
            writer.write("/stop");

            writer.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */

    }

}
