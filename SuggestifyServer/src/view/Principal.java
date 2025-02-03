package view;

import java.net.ServerSocket;
import java.net.Socket;
import factory.Conector;
import controller.TrataClienteController;

public class Principal {
    public static void main(String[] args) {
        try {
            // testando a conexão com o banco
            if (Conector.getConnection() != null){
                System.out.println("Conexão com o banco efetuada com sucesso!");
            }
            
            //Iremos declarar o servidor socket
            ServerSocket servidor = new ServerSocket(12345);
            System.out.println("Servidor inicializado! Aguardando conexões...");
            int idUnico = 0; // codigo para cada cliente
            while(true){ // looping infinito
                //a linha abaixo recebe a conexão do cliente
                Socket cliente = servidor.accept();
                System.out.println("Um novo cliente se conectou: "+cliente);
                idUnico++;
                // instanciar uma thread para que o cliente possa executar em separado 
                // de todos os outros clientes
                TrataClienteController tratacliente = new TrataClienteController(idUnico, cliente);
                tratacliente.start();// inicia a thread.  o start() executa o método RUN
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
