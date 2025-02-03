package view;

import controller.ConexaoController;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Principal {
    public static ConexaoController ccont;
    
    public static void main(String[] args) {
        try {
            // criar a conexão com o Servidor
            // ao inves de usar localhost eu posso usa 127.0.0.1
            Socket socket = new Socket("127.0.0.1",12345);
            // com o OUT eu posso enviar coisas para o SERVIDOR
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            // com o IN eu posso receber coisas do SERVIDOR
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            
            // instanciando o controller
            ccont = new ConexaoController(in, out);
            // Todo: Temos que abrir a tela de login
            TelaInicial telaInicial = new TelaInicial();
            telaInicial.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
