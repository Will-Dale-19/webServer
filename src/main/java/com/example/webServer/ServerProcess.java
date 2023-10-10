package com.example.webServer;

import java.io.*;

public class ServerProcess {
    ProcessBuilder pb;
    String serverLocation;

    public ServerProcess(String serverLocation){
        this.serverLocation = serverLocation;
    }


    public Process startServer(){
        pb = new ProcessBuilder(serverLocation + "\\start.bat");
        File log = new File(serverLocation + "\\log");
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
        try {
            Process p = pb.start();
            assert pb.redirectOutput().file() == log;
            return p;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopServer(Process p){
        PrintWriter stdin = new PrintWriter(
                new BufferedWriter(p.outputWriter()));

        stdin.println("/stop");
    }

}
