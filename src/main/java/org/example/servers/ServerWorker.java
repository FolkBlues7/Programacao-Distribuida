package org.example.servers;

import org.example.Utils.NetworkInfo;
import org.example.runnables.ServerWorkerRunnable;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerWorker {

    public static void main()  {

        int workersQuantity = 8; //também define a quantidade de threads para worker.

        try (ExecutorService executorService = Executors.newFixedThreadPool(workersQuantity)){

            for (int i = 0; i< workersQuantity; i++){
                //Esse construtor enorme é o construtor de um runnable de Worker.
                ServerWorkerRunnable serverWorkerRunnable = new ServerWorkerRunnable(new Socket(NetworkInfo.localhost, NetworkInfo.ServerManagerPort),
                        new Socket(NetworkInfo.localhost, NetworkInfo.ServerDataPort));

                executorService.submit(serverWorkerRunnable);
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
