package org.example.runnables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ServerDataRunnable implements Runnable {

    private final Socket clientSocket;
    private final Map<String, List<String>> database;

    public ServerDataRunnable(Socket clientSocket, Map<String, List<String>> database){
        this.clientSocket = clientSocket;
        this.database = database; //esta recebendo a referência, o que está tudo bem, pois nunca irá modificar o MAP
    }

    @Override
    public void run() {

        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));){

            //formato da mensagem: GET/URL1, então parts[0] armazena o tipo da requisição e parts[1] a chave
            String msg = "";
            while ((msg = in.readLine()) != null){
                if (msg.contains("GET")){ //retorna a lista das hiperligações associados a chave
                    String[] parts = msg.split("/");
                    out.println(database.get(parts[1]));
                } else if (msg.contains("LINKS")){ //retorna a lista de chaves
                    //out.println(database.keySet());
                    out.println(String.join(",", database.keySet()));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
