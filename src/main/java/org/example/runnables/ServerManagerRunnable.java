package org.example.runnables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ServerManagerRunnable implements Runnable{

    private final Socket workerSocket;
    private final BlockingQueue<String> fila;
    private final ConcurrentHashMap<String, Boolean> urlVisitadas;

    public ServerManagerRunnable(Socket workerSocket, BlockingQueue<String> fila, ConcurrentHashMap<String, Boolean> urlVisitadas) {
        this.workerSocket = workerSocket;
        this.fila = fila;
        this.urlVisitadas = urlVisitadas;
    }

    public void stop(){

    }

    @Override
    public void run() {

        try(PrintWriter out = new PrintWriter(workerSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(workerSocket.getInputStream()));){


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
