package org.example.servers;

/*
    Seguindo a arquitetura passada pelo professor está classe
    será responsável por ler de ServerData.txt e guardar os
    valores em Map<String, List<String> = new Hashmap<>; de
    modo a responder via Sockets as requisições dos Workers
    por hyperlinks para buscar por mais hyperlinks.
 */

import org.example.Utils.NetworkInfo;
import org.example.runnables.ServerDataRunnable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerData {

    public static void main(String[] args) throws IOException {

        String ServerDataPath = NetworkInfo.ServerDataPath;

        Map<String, List<String>> database = new HashMap<>();

        //esse try with resources lê as linhas das URLs contidas em ServerDataPath e guarda no HashMap
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(ServerDataPath))){

            String line = "";
            while ((line = bufferedReader.readLine()) != null){   //pega a linha completa
                String[] parts = line.split(";");           //divide em três partes "ID" - URL - LISTA DE URLS
                database.put(parts[1], List.of(parts[2].split(","))); //parts[1] URL do site e parts[2] lista de URLs associadas
            }

           for (String chave: database.keySet()){
               System.out.println(chave);
           }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //esse try serve para conectar ao servidor e também criar o threadpool
        try(ServerSocket serverSocket = new ServerSocket(NetworkInfo.ServerDataPort);
            ExecutorService executorService = Executors.newSingleThreadExecutor()){

            while (true){
                executorService.submit(new ServerDataRunnable(serverSocket.accept(), database));
                System.out.println("Alguém se conectou!");
            }

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }


    }

}
