/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.io.File;
import javax.swing.JOptionPane;
import modelDominio.Artista;
import modelDominio.Comunidade;
import view.util.Colors;
import view.util.Imagem;

/**
 *
 * @author lucas
 */
public class TelaCadastro extends javax.swing.JFrame {

    /**
     * Creates new form TelaCadastro
     */
    public TelaCadastro() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jtxtNomeArtistico = new javax.swing.JTextField();
        jtxtUsuario = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtxtEmail = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtxtSenha = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        jtxtSenhaConfirmar = new javax.swing.JPasswordField();
        jbtnCriarConta = new javax.swing.JButton();
        jbtnCriarConta1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(10, 10, 10));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/images/logoRoxoComNomeMaisPequena.png"))); // NOI18N

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nome artístico:");

        jtxtNomeArtistico.setBackground(new java.awt.Color(51, 51, 51));
        jtxtNomeArtistico.setForeground(new java.awt.Color(255, 255, 255));
        jtxtNomeArtistico.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jtxtNomeArtistico.setCaretColor(new java.awt.Color(102, 102, 102));

        jtxtUsuario.setBackground(new java.awt.Color(51, 51, 51));
        jtxtUsuario.setForeground(new java.awt.Color(255, 255, 255));
        jtxtUsuario.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jtxtUsuario.setCaretColor(new java.awt.Color(102, 102, 102));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Usuário:");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("E-mail:");

        jtxtEmail.setBackground(new java.awt.Color(51, 51, 51));
        jtxtEmail.setForeground(new java.awt.Color(255, 255, 255));
        jtxtEmail.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jtxtEmail.setCaretColor(new java.awt.Color(102, 102, 102));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Senha:");

        jtxtSenha.setBackground(new java.awt.Color(51, 51, 51));
        jtxtSenha.setForeground(new java.awt.Color(255, 255, 255));
        jtxtSenha.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jtxtSenha.setCaretColor(new java.awt.Color(102, 102, 102));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Confirmar senha:");

        jtxtSenhaConfirmar.setBackground(new java.awt.Color(51, 51, 51));
        jtxtSenhaConfirmar.setForeground(new java.awt.Color(255, 255, 255));
        jtxtSenhaConfirmar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jtxtSenhaConfirmar.setCaretColor(new java.awt.Color(102, 102, 102));

        jbtnCriarConta.setBackground(new java.awt.Color(80, 0, 198));
        jbtnCriarConta.setForeground(new java.awt.Color(255, 255, 255));
        jbtnCriarConta.setText("Criar conta");
        jbtnCriarConta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCriarContaActionPerformed(evt);
            }
        });

        jbtnCriarConta1.setBackground(new java.awt.Color(80, 0, 198));
        jbtnCriarConta1.setForeground(new java.awt.Color(255, 255, 255));
        jbtnCriarConta1.setText("Voltar");
        jbtnCriarConta1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCriarConta1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnCriarConta1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(651, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jtxtSenhaConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jtxtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jtxtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jtxtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(277, 277, 277)
                                .addComponent(jLabel7))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtxtNomeArtistico, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(276, 276, 276)
                        .addComponent(jbtnCriarConta, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(241, 241, 241)))
                .addGap(63, 63, 63))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnCriarConta1)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtxtNomeArtistico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtxtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtxtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtxtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtxtSenhaConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(jbtnCriarConta, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnCriarContaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCriarContaActionPerformed
        String senhaCripto = util.Criptografia.criptografarSenha(String.valueOf(jtxtSenha.getPassword()));
        String senhaConfirmarCripto = util.Criptografia.criptografarSenha(String.valueOf(jtxtSenhaConfirmar.getPassword()));
        if (senhaCripto.equals(senhaConfirmarCripto)) {
            Artista artista = null;
            //pega o pfp padrão para o usuário
            try {
                artista = new Artista(jtxtUsuario.getText(), jtxtEmail.getText(), senhaCripto, jtxtNomeArtistico.getText(),
                        getClass().getResourceAsStream("/view/images/defaultPfp.jpg").readAllBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }

            boolean result = Principal.ccont.cadastrarUsuario(artista);
            if (result) {
                JOptionPane.showMessageDialog(rootPane, "Cadastrado!");
                //logar o artista criado para pegar o id dele
                //para então criar a comunidade com o respectivo id como fk
                artista = (Artista) Principal.ccont.efetuarLogin(artista);
                System.out.println("usuário logado: " + artista.getNomeUsuario());
                byte[] imagem = null;
                //pega o background padrão para a comunidade
                try {
                    imagem = getClass().getResourceAsStream("/view/images/logoRoxoForte.png").readAllBytes();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                //coloca as cores padrão da comunidade
                Colors colorConverter = new Colors();
                                
                //cria comunidade
                boolean resultCom = Principal.ccont.criarComunidade(artista, imagem, "0x5755DF", "0xFFFFFF");

                if (resultCom) {
                    JOptionPane.showMessageDialog(rootPane, "Comunidade criada com sucesso!");
                    Comunidade com = Principal.ccont.getComunidadePeloArtistaId(artista.getCodUsuario());
                    if (com != null) {
                        TelaComunidade telaComunidade = new TelaComunidade(com);
                        telaComunidade.setLocationRelativeTo(null);
                        telaComunidade.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Falha ao carregar comunidade!");
                    }

                } else {
                    JOptionPane.showMessageDialog(rootPane, "Erro ao criar comunidade");
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Erro ao cadastrar");
            }
        }
    }//GEN-LAST:event_jbtnCriarContaActionPerformed

    private void jbtnCriarConta1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCriarConta1ActionPerformed
        TelaInicial telaInicial = new TelaInicial();
        telaInicial.setVisible(true);
        dispose();
    }//GEN-LAST:event_jbtnCriarConta1ActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseClicked

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(TelaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaCadastro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbtnCriarConta;
    private javax.swing.JButton jbtnCriarConta1;
    private javax.swing.JTextField jtxtEmail;
    private javax.swing.JTextField jtxtNomeArtistico;
    private javax.swing.JPasswordField jtxtSenha;
    private javax.swing.JPasswordField jtxtSenhaConfirmar;
    private javax.swing.JTextField jtxtUsuario;
    // End of variables declaration//GEN-END:variables
}
