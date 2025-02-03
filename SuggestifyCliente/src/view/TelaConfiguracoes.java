/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import modelDominio.Comunidade;

/**
 *
 * @author lucas
 */
public class TelaConfiguracoes extends javax.swing.JFrame {

    /**
     * Creates new form TelaConfiguracoes
     */
    private Comunidade comunidade;
    
    public TelaConfiguracoes(Comunidade c) {
        this.comunidade = c;
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
        jbtnExcluirConta = new javax.swing.JButton();
        jbtnMudarSenha = new javax.swing.JButton();
        jbtnMudarEmail = new javax.swing.JButton();
        jbtnMudarNomeUsuario = new javax.swing.JButton();
        jbtnHome = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(10, 10, 10));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/images/logoRoxoComNomeMaisPequena.png"))); // NOI18N
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(352, 6, -1, -1));

        jbtnExcluirConta.setBackground(new java.awt.Color(223, 85, 92));
        jbtnExcluirConta.setForeground(new java.awt.Color(255, 255, 255));
        jbtnExcluirConta.setText("Excluir conta");
        jbtnExcluirConta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExcluirContaActionPerformed(evt);
            }
        });
        jPanel1.add(jbtnExcluirConta, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 380, 320, 30));

        jbtnMudarSenha.setBackground(new java.awt.Color(80, 0, 198));
        jbtnMudarSenha.setForeground(new java.awt.Color(255, 255, 255));
        jbtnMudarSenha.setText("Mudar senha");
        jbtnMudarSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnMudarSenhaActionPerformed(evt);
            }
        });
        jPanel1.add(jbtnMudarSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 320, 320, 30));

        jbtnMudarEmail.setBackground(new java.awt.Color(80, 0, 198));
        jbtnMudarEmail.setForeground(new java.awt.Color(255, 255, 255));
        jbtnMudarEmail.setText("Mudar e-mail");
        jbtnMudarEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnMudarEmailActionPerformed(evt);
            }
        });
        jPanel1.add(jbtnMudarEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 260, 320, 30));

        jbtnMudarNomeUsuario.setBackground(new java.awt.Color(80, 0, 198));
        jbtnMudarNomeUsuario.setForeground(new java.awt.Color(255, 255, 255));
        jbtnMudarNomeUsuario.setText("Mudar nome de usuario");
        jbtnMudarNomeUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnMudarNomeUsuarioActionPerformed(evt);
            }
        });
        jPanel1.add(jbtnMudarNomeUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 320, 30));

        jbtnHome.setBackground(new java.awt.Color(80, 0, 198));
        jbtnHome.setForeground(new java.awt.Color(255, 255, 255));
        jbtnHome.setText("Voltar");
        jbtnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnHomeActionPerformed(evt);
            }
        });
        jPanel1.add(jbtnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 130, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnExcluirContaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExcluirContaActionPerformed

        //0 = mudar nome
        //1 = mudar email
        //2 = mudar senha
        //3 = excluir conta
        DialogConfirmar dc = new DialogConfirmar(this, this.comunidade, 3);
        //abrir só uma vez
        dc.setModal(true);
        dc.setVisible(true);
    }//GEN-LAST:event_jbtnExcluirContaActionPerformed

    private void jbtnMudarSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnMudarSenhaActionPerformed
        //0 = mudar nome
        //1 = mudar email
        //2 = mudar senha
        //3 = excluir conta
        DialogConfirmar dc = new DialogConfirmar(this, this.comunidade, 2);
        //abrir só uma vez
        dc.setModal(true);
        dc.setVisible(true);
    }//GEN-LAST:event_jbtnMudarSenhaActionPerformed

    private void jbtnMudarEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnMudarEmailActionPerformed
        //0 = mudar nome
        //1 = mudar email
        //2 = mudar senha
        //3 = excluir conta
        DialogConfirmar dc = new DialogConfirmar(this, this.comunidade, 1);
        //abrir só uma vez
        dc.setModal(true);
        dc.setVisible(true);
    }//GEN-LAST:event_jbtnMudarEmailActionPerformed

    private void jbtnMudarNomeUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnMudarNomeUsuarioActionPerformed
        //0 = mudar nome
        //1 = mudar email
        //2 = mudar senha
        //3 = excluir conta
        DialogConfirmar dc = new DialogConfirmar(this, this.comunidade, 0);
        //abrir só uma vez
        dc.setModal(true);
        dc.setVisible(true);
    }//GEN-LAST:event_jbtnMudarNomeUsuarioActionPerformed

    private void jbtnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnHomeActionPerformed
        TelaComunidade tc = new TelaComunidade(comunidade);
        tc.setLocationRelativeTo(null);
        tc.setVisible(true);
        dispose();
    }//GEN-LAST:event_jbtnHomeActionPerformed

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
                new TelaConfiguracoes(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbtnExcluirConta;
    private javax.swing.JButton jbtnHome;
    private javax.swing.JButton jbtnMudarEmail;
    private javax.swing.JButton jbtnMudarNomeUsuario;
    private javax.swing.JButton jbtnMudarSenha;
    // End of variables declaration//GEN-END:variables
}
