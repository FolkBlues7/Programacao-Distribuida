package org.example.servers;

import org.example.Utils.NetworkInfo;
import org.example.runnables.ServerManagerRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ServerManager {


    public static void main(String[] args) throws IOException {

        final BlockingQueue<String> fila;
        final ConcurrentHashMap<String, Boolean> urlVisitadas = new ConcurrentHashMap<>();

        //Essa parte conecta-se com o ServerData e lê todas as chaves das URLs e salva na fila
        //Depois fecha a conexão com o ServerData
        Socket serverData = new Socket(NetworkInfo.localhost, NetworkInfo.ServerDataPort);

        PrintWriter out = new PrintWriter(serverData.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(serverData.getInputStream()));

        out.println("LINKS");
        //difícil de ler :)
        fila = new LinkedBlockingQueue<>(List.of(in.readLine().split(",")));
        serverData.close();
        out.close();
        in.close();

        //Essa parte é quem cria o servidor e fica a escutar pelos Workers
        try(ServerSocket serverSocket = new ServerSocket();ExecutorService executorService = Executors.newFixedThreadPool(8)){
            while (true) {
                ServerManagerRunnable serverManagerRunnable = new ServerManagerRunnable(serverSocket.accept(), fila, urlVisitadas);
                executorService.submit(serverManagerRunnable);
            }
        }
    }
}
