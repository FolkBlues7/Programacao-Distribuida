package org.example.mock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateData {

    /*
    Essa classe é destinada a gerar os dados das URL junto dos seus nomes e lista de hiperligações relacionados
    seguindo o formato: ID_DA_PAGINA;TEXTO_DA_PAGINA;LINKS_SEPARADOS_POR_VIRGULA.
     */

    public static void main(String[] args){

        final int totalPagina = 10;
        final int urlPorPagina = 10;

        final String fileName = "ServerData";

        try (FileWriter fileWriter = new FileWriter(fileName); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 1; i <= totalPagina; i++){
                stringBuilder.append(String.format("%d;URL%d;", i, i));
                for (int j = 1; j <= urlPorPagina; j++){
                    stringBuilder.append("URL").append(i*urlPorPagina+j);
                    if (j < urlPorPagina){
                        stringBuilder.append(",");
                    }
                }

                bufferedWriter.write(String.valueOf(stringBuilder));
                bufferedWriter.newLine(); //poderia ser o \n também, mas assim é mais fácil de ler
                stringBuilder.setLength(0);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
