package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import modelDominio.Artista;
import modelDominio.Comunidade;
import util.Criptografia;

public class DialogConfirmar extends JDialog {

    //0 = mudar nome
    //1 = mudar email
    //2 = mudar senha
    //3 = excluir conta
    private int tipo;
    private Comunidade comunidade;
    private TelaConfiguracoes telaConfiguracoes;
    private JTextField txt1, txt2;
    private JButton btnCancelar, btnConfirmar;

    //passando a TelaConfiguracoes como parâmetro para manipular a comunidade dela em caso de update 
    //e para fechar em caso de excluir a conta
    public DialogConfirmar(TelaConfiguracoes telaConfiguracoes, Comunidade comunidade, int tipo) {
        this.telaConfiguracoes = telaConfiguracoes;
        this.comunidade = comunidade;
        this.tipo = tipo;
        this.setTitle("Confirmação");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.setIconImage(new ImageIcon(getClass().getResource("/view/images/logoRoxoComNome.png")).getImage());
        this.getContentPane().setBackground(new Color(40, 40, 40));
        this.setSize(new Dimension(500, 200));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Define a forma arredondada usando RoundRectangle2D
        this.setUndecorated(true); // Remove a decoração padrão (borda, barra de título)
        Shape roundedShape = new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), 50, 50);
        this.setShape(roundedShape);
        this.setLocationRelativeTo(null);

        JPanel tituloPanel = new JPanel(new FlowLayout());
        tituloPanel.setOpaque(false);
        tituloPanel.setPreferredSize(new Dimension(500, 50));

        JLabel lblTitulo = new JLabel();
        lblTitulo.setForeground(Color.white);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        if (this.tipo == 3) {
            lblTitulo.setText("Você quer mesmo excluir sua conta?");
        } else {
            lblTitulo.setText("Confirme que é você");
        }
        tituloPanel.add(lblTitulo);

        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(tituloPanel);
        this.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel panelTextField1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelTextField1.setOpaque(false);
        panelTextField1.setMaximumSize(new Dimension(500, 50));

        JLabel lbl1 = new JLabel();
        lbl1.setForeground(Color.white);
        lbl1.setFont(new Font("Arial", Font.BOLD, 14));

        panelTextField1.add(lbl1);

        txt1 = new JTextField();
        txt1.setPreferredSize(new Dimension(300, 20));
        txt1.setBackground(new Color(30, 30, 30));
        txt1.setForeground(Color.white);
        txt1.setFont(new Font("Arial", Font.BOLD, 14));

        panelTextField1.add(txt1);

        this.add(panelTextField1);
        this.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel panelTextField2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelTextField2.setOpaque(false);
        panelTextField2.setMaximumSize(new Dimension(500, 50));

        JLabel lbl2 = new JLabel();
        lbl2.setForeground(Color.white);
        lbl2.setFont(new Font("Arial", Font.BOLD, 14));

        panelTextField2.add(lbl2);

        txt2 = new JTextField();
        txt2.setPreferredSize(new Dimension(300, 20));
        txt2.setBackground(new Color(30, 30, 30));
        txt2.setForeground(Color.white);
        txt2.setFont(new Font("Arial", Font.BOLD, 14));

        panelTextField2.add(txt2);
        this.add(panelTextField2);

        JPanel holderBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 110, 15));
        holderBotoes.setPreferredSize(new Dimension(this.getWidth(), 100));
        holderBotoes.setOpaque(false);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(120, 30));
        btnCancelar.setBackground(new Color(0x323232));
        btnCancelar.setForeground(Color.white);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));

        btnCancelar.addActionListener((e) -> {
            btnCancelarClicado(e);
        });

        holderBotoes.add(btnCancelar);

        btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setPreferredSize(new Dimension(120, 30));
        btnConfirmar.setBackground(new Color(0x5755DF));
        btnConfirmar.setForeground(Color.white);
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 14));

        btnConfirmar.addActionListener((e) -> {
            btnConfirmarClicado(e);
        });

        holderBotoes.add(btnConfirmar);

        this.add(holderBotoes);

        //0 = mudar nome
        //1 = mudar email
        //2 = mudar senha
        //3 = excluir conta
        switch (this.tipo) {
            case 0:
                lbl1.setText("Novo username:");
                lbl2.setText("Senha:");
                lbl2.setBorder(new EmptyBorder(0, 65, 0, 0));
                break;
            case 1:
                lbl1.setText("Novo e-mail:");
                lbl2.setText("Senha:");
                lbl2.setBorder(new EmptyBorder(0, 40, 0, 0));
                break;
            case 2:
                lbl1.setText("Nova senha:");
                lbl2.setText("Senha atual:");
                lbl2.setBorder(new EmptyBorder(0, 5, 0, 0));
                break;
            case 3:
                lbl1.setText("Usuario:");
                lbl2.setText("Senha:");
                lbl2.setBorder(new EmptyBorder(0, 10, 0, 0));
                btnConfirmar.setBackground(new Color(0xDF555C));
                break;
        }
    }

    public void btnCancelarClicado(ActionEvent e) {
        dispose();
    }

    public void btnConfirmarClicado(ActionEvent e) {
        //0 = mudar nome
        //1 = mudar email
        //2 = mudar senha
        //3 = excluir conta

        if (!txt1.equals("") && !txt2.equals("")) {
            if (Principal.ccont.confirmarSenha(this.comunidade.getArtista(), Criptografia.criptografarSenha(txt2.getText()))) {
                switch (this.tipo) {
                    case 0, 1, 2:
                        Artista newA;
                        switch (this.tipo) {
                            case 0:
                                newA = new Artista(this.comunidade.getArtista().getCodUsuario(),
                                        txt1.getText(),
                                        this.comunidade.getArtista().getEmail(),
                                        this.comunidade.getArtista().getSenha(),
                                        this.comunidade.getArtista().getNomeArtistico(),
                                        this.comunidade.getArtista().getFotoPerfil(),
                                        this.comunidade.getArtista().getBio());
                                break;
                                
                            case 1:
                                newA = new Artista(this.comunidade.getArtista().getCodUsuario(),
                                        this.comunidade.getArtista().getNomeUsuario(),
                                        txt1.getText(),
                                        this.comunidade.getArtista().getSenha(),
                                        this.comunidade.getArtista().getNomeArtistico(),
                                        this.comunidade.getArtista().getFotoPerfil(),
                                        this.comunidade.getArtista().getBio());
                                break;
                                
                            case 2:
                                newA = new Artista(this.comunidade.getArtista().getCodUsuario(),
                                        this.comunidade.getArtista().getNomeUsuario(),
                                        this.comunidade.getArtista().getEmail(),
                                        Criptografia.criptografarSenha(txt1.getText()),
                                        this.comunidade.getArtista().getNomeArtistico(),
                                        this.comunidade.getArtista().getFotoPerfil(),
                                        this.comunidade.getArtista().getBio());
                                break;
                            default:
                                newA = this.comunidade.getArtista();
                                break;
                        }
                        if (Principal.ccont.editarUsuario(newA)) {
                            JOptionPane.showMessageDialog(rootPane, "Sucesso!");
                            this.comunidade.setArtista(newA); 
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(rootPane, "Erro!");
                        }

                        break;

                    case 3://3 = excluir conta
                        if(Principal.ccont.deleteUsuario(this.comunidade.getArtista())){
                            TelaInicial ti = new TelaInicial();
                            ti.setVisible(true);
                            dispose();
                            telaConfiguracoes.dispose();
                        }
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Senha incorreta!");
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Preencha os campos");
        }
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaConfiguracoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaConfiguracoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaConfiguracoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaConfiguracoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DialogConfirmar(null, null, -1).setVisible(true);
            }
        });
    }

}
