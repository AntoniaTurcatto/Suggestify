package view.util;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import modelDominio.Album;

public class ComboBoxAlbum {

    private String value; // String que aparece para o usuário
    private int key;      // Código da String (Chave primaria da tabela Album)

    public ComboBoxAlbum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    // sobrescrevendo o método padrão toString que facilita para imprimir valores na tela,
    // sem esse método são impressos os endereços da memória onde o objeto foi declarado.
    @Override
    public String toString() {
        return value;
    }

    // método get a chave primária
    public int getKey() {
        return key;
    }

    // Este método retorna o código (chave primária) da opção que está selecionada pelo usuário
    public static int getSelectedIndex(JComboBox combo) {
        Object obj = combo.getSelectedItem();
        if (obj == null) {
            return -1; // se nenhum item estiver selecionado devolve -1
        } else {
            return ((ComboBoxAlbum) obj).getKey();// devolve o código da chave selecionada
        }
    }
    
    // Método que preenche o comboBox com os nomes das Marcas.
    // selCodigo --> parâmetro para já selecionar um código 
    // combo     --> ComboBox a ser preenchida
    // lista     --> Lista de Raças
    public static void preencheComboBoxMarca(int SelCodigo, JComboBox combo, ArrayList<Album> lista, Boolean addTodos){
        Vector vlista= new Vector();
        int x = 0;  // se quiser inserir um vazio então tem que jogar esse número para 1
        int selecionarCodigo = -1; // guarda a posicao se tiver código selecionado
        
        /* inserindo vazio*/
        // só habilite a linha abaixo se você quiser uma Marca vazia, exemplo:
        // cadastrar sem Marca.
        if (addTodos)
            vlista.add(new ComboBoxAlbum(0,"Todas"));
            
        // percorre toda a lista de album e armazena as informações no Vector vlista;
        for (Album a: lista){ 
            // criando um ComboBoxMarca
            ComboBoxAlbum cmb = new ComboBoxAlbum(a.getIdAlbum(),a.getNome());
            // armazenando esse comboBoc no Vector
            vlista.add(cmb);
            // Se o código atual for igual ao código que já se deseja pré-selecionar
            // guarda-se a informação em selecionarCodigo
            if (cmb.getKey() == SelCodigo)
                selecionarCodigo = x;
            x++;// incrementa a variável x que guarda o índice que é diferente do código da tabela
        }
        
        // Criando um modelo com o vetor
        DefaultComboBoxModel modelocliente = new DefaultComboBoxModel(vlista);
        // setando o modelo no ComboBox
        combo.setModel(modelocliente);
        // Selecionando o índice que o usuário selecionou como padrão.
        if (selecionarCodigo >= 0)
            combo.setSelectedIndex(selecionarCodigo);
    }
}
