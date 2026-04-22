package org.example.runnables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
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

        String threadAtual = Thread.currentThread().getName();

        try(PrintWriter out = new PrintWriter(workerSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(workerSocket.getInputStream()));){

            while (!fila.isEmpty()) {
                String urlTarget = fila.take(); //tira o primeiro da fila
                System.out.println(threadAtual + ": Vai enviar: " + urlTarget + "para o Worker");
                out.println(urlTarget);         //envia para o worker

                String msg = in.readLine();     //recebe a resposta com todas as urls do worker
                if (!msg.isEmpty()){
                    List<String> urlResult = List.of(msg.split(","));

                    //aqui poderia ser só um for each e não ter o distinct que teria o mesmo efeito
                    urlResult.stream().distinct().forEach(url -> {
                        if (!fila.contains(url)){
                            fila.add(url);
                            System.out.println(threadAtual + ": Adicionei o link: " + url + " a fila.");
                        }
                    });

                }

                urlVisitadas.putIfAbsent(urlTarget, true);

            }
            out.println(); //envia uma mensagem vazia e finaliza a operação
            System.out.println("O tamanho final da lista de urls distintas é: " + urlVisitadas.size());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
