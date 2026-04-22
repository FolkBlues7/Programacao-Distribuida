package org.example.runnables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ServerWorkerRunnable implements Runnable{

    private final Socket managerSocket; //vai se comunicar só com o coordenador
    private final Socket databaseSocket; // vai se comunicar só com o DatabaseServer

    public ServerWorkerRunnable(Socket managerSocket, Socket databaseSocket){
        this.managerSocket = managerSocket;
        this.databaseSocket = databaseSocket;
    }

    @Override
    public void run() {

        String threadName = Thread.currentThread().getName();

        try(PrintWriter outManager = new PrintWriter(managerSocket.getOutputStream(), true);
            BufferedReader inManager = new BufferedReader(new InputStreamReader(managerSocket.getInputStream()));
            PrintWriter outDatabase = new PrintWriter(databaseSocket.getOutputStream(), true);
            BufferedReader inDatabase = new BufferedReader(new InputStreamReader(databaseSocket.getInputStream()));){

            String msg;
            while ((msg = inManager.readLine()) != null){
                outDatabase.println("GET/"+msg);
                String resposta = inDatabase.readLine();
                if (resposta == null){
                    System.out.println( threadName + ": O endereço: " + msg + " não possui links adjacentes");
                    outManager.println();
                } else {
                    System.out.println(threadName + ": Visitando " + msg);
                    System.out.println(threadName + ": Na " +  msg + " encontramos: " + resposta);
                    List<String> listaSeparada = List.of(resposta.split(","));
                    listaSeparada = listaSeparada.stream().distinct().map(String::toUpperCase).toList();
                    outManager.println(String.join(",", listaSeparada)); //Precisa disso para não mandar [ ]
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
