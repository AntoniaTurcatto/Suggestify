package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import modelDominio.Artista;
import modelDominio.Comunidade;
import view.util.Imagem;

public class TelaEditarComunidade extends JFrame {

    private Comunidade comunidade;
    JButton btnVoltar, btnAlbuns, btnMusicas, btnTags, btnEditar;
    JLabel lblSair;
    private JFileChooser fileChooserImagem;
    private JPanelCustomizado backgroundPreviewPanel, fotoPreviewPanel;
    private JTextField txtNovoNome, txtNovaCorFundoPost, txtNovaCorFontePost;
    private JTextArea txtBio;

    public TelaEditarComunidade(Comunidade c) {
        this.comunidade = c;
        fileChooserImagem = new javax.swing.JFileChooser();
        this.setTitle("Editar Comunidade");
        this.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        getContentPane().setLayout(
                new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)
        );
        this.setIconImage(new ImageIcon(getClass().getResource("/view/images/logoRoxoComNome.png")).getImage());
        this.getContentPane().setBackground(new Color(30, 30, 30));
        this.setMinimumSize(new Dimension(900, 700));

        //------------ HEADER----------------
        //jPanel do header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0x5755DF));
        //x,y,width, height
        headerPanel.setPreferredSize(new Dimension(900, 25));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        headerPanel.setLayout(new BorderLayout());
        this.add(headerPanel, BorderLayout.NORTH);

        //botao de configuracoes do header
        btnVoltar = new JButton();
        btnVoltar.setPreferredSize(new Dimension(25, 25));
        btnVoltar.addActionListener((ActionEvent e) -> {
            btnVoltarClicado(e);
        });
        headerPanel.add(btnVoltar, BorderLayout.WEST);

        // Painel intermediário para centralizar a imagem
        JPanel headerCenterPanel = new JPanel();
        headerCenterPanel.setOpaque(false); // Deixa o painel transparente para ver o fundo
        headerPanel.add(headerCenterPanel, BorderLayout.CENTER);

        //imagem do headerPanel adicionada ao panel intermediário centralizado
        //é necessário esse panel intermediário para que a imagem
        //não se estique ou ocupe espaço indesejado
        ImageIcon logoHeader = new ImageIcon(getClass().getResource("/view/images/logoBrancaSomenteNomePequeno.png"));
        JLabel imageLabelHeader = new JLabel(logoHeader);
        headerCenterPanel.add(imageLabelHeader);

        //jLabel clicável para sair
        lblSair = new JLabel("Sair     "); // espaço funcionando como margem
        lblSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                lblSairClicado(e);
            }
        });
        lblSair.setForeground(Color.white);
        headerPanel.add(lblSair, BorderLayout.EAST);

        //fim header
        //BANNER
        //=============================================
        JPanel banner = new JPanel();
        banner.setLayout(new BoxLayout(banner, BoxLayout.X_AXIS));
        banner.setPreferredSize(new Dimension(900, 300));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        banner.setBackground(new Color(0x5755DF));

        //elementos banner-------------------------------
        //fotoArtista
        RoundedImagePanel fotoComunidade = new RoundedImagePanel(comunidade.getArtista().getFotoPerfil(),
                new Dimension(135, 135), new Dimension(135, 135), new Dimension(135, 135));

        JPanel panelMargin1 = new JPanel();
        panelMargin1.setOpaque(false);
        panelMargin1.setPreferredSize(new Dimension(115, 0));

        JPanel panelMargin2 = new JPanel();
        panelMargin2.setOpaque(false);
        panelMargin2.setPreferredSize(new Dimension(115 + 185, 0));

        banner.add(Box.createRigidArea(new Dimension(50, 0)));

        banner.add(fotoComunidade);

        banner.add(panelMargin1);

        //holder do titulo do banner
        JPanel holderTextoBanner = new JPanel();
        holderTextoBanner.setLayout(new BoxLayout(holderTextoBanner, BoxLayout.Y_AXIS));
        holderTextoBanner.setOpaque(false);
        holderTextoBanner.setPreferredSize(new Dimension(300, 250));

        JLabel lblNomeComunidade = new JLabel(comunidade.getArtista().getNomeArtistico());
        lblNomeComunidade.setForeground(new Color(10, 10, 10));
        lblNomeComunidade.setAlignmentX(CENTER_ALIGNMENT);
        lblNomeComunidade.setFont(new Font("Arial", Font.BOLD, 18));

        holderTextoBanner.add(lblNomeComunidade);

        JLabel lblEditar = new JLabel("Editar");
        lblEditar.setForeground(new Color(10, 10, 10));
        lblEditar.setAlignmentX(CENTER_ALIGNMENT);
        lblEditar.setFont(new Font("Arial", Font.ITALIC, 50));

        holderTextoBanner.add(Box.createRigidArea(new Dimension(0, 8)));

        holderTextoBanner.add(lblEditar);

        banner.add(holderTextoBanner);

        banner.add(panelMargin2);
        this.add(banner);

        //fim banner======================================
        //corpo ===============================================
        //margem
        JPanel margemParaOCorpoPanel = new JPanel();
        margemParaOCorpoPanel.setPreferredSize(new Dimension(this.getWidth(), 50));
        margemParaOCorpoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        margemParaOCorpoPanel.setOpaque(false);
        this.add(margemParaOCorpoPanel);

        //nome da comunidade editar -----------------------------
        JPanel holderNomeDaComunidadeEditar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        holderNomeDaComunidadeEditar.setPreferredSize(new Dimension(this.getWidth(), 50));
        holderNomeDaComunidadeEditar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        holderNomeDaComunidadeEditar.setOpaque(false);

        JLabel lblNomeComunidadeCorpo = new JLabel("Nome da comunidade: ");
        lblNomeComunidadeCorpo.setForeground(Color.white);
        lblNomeComunidadeCorpo.setFont(new Font("Arial", Font.BOLD, 12));
        holderNomeDaComunidadeEditar.add(lblNomeComunidadeCorpo);

        txtNovoNome = new JTextField();
        txtNovoNome.setPreferredSize(new Dimension(400, 25));
        txtNovoNome.setBackground(new Color(80, 80, 80));
        txtNovoNome.setForeground(Color.white);
        txtNovoNome.setBorder(new EmptyBorder(0, 0, 0, 0));
        txtNovoNome.setText(comunidade.getArtista().getNomeArtistico());
        holderNomeDaComunidadeEditar.add(txtNovoNome);

        this.add(holderNomeDaComunidadeEditar);
        //margem 
//        JPanel margemParaOCorpoPosNomePanel = new JPanel();
//        margemParaOCorpoPosNomePanel.setPreferredSize(new Dimension(this.getWidth(), 50));
//        margemParaOCorpoPosNomePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
//        margemParaOCorpoPosNomePanel.setOpaque(true);
//        this.add(margemParaOCorpoPosNomePanel);

        //editar BIO-------------------------------------------------
        JPanel holderBioEditar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        holderBioEditar.setPreferredSize(new Dimension(this.getWidth(), 80));
        holderBioEditar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        holderBioEditar.setOpaque(false);

        JPanel margemBioPanel = new JPanel();
        margemBioPanel.setPreferredSize(new Dimension(135, 0));
        holderBioEditar.add(margemBioPanel);

        JLabel lblBioCorpo = new JLabel("Bio: ");
        lblBioCorpo.setForeground(Color.white);
        lblBioCorpo.setFont(new Font("Arial", Font.BOLD, 12));
        holderBioEditar.add(lblBioCorpo);

        txtBio = new JTextArea();
        txtBio.setPreferredSize(new Dimension(400, 150));
        txtBio.setLineWrap(true);
        txtBio.setBackground(new Color(80, 80, 80));
        txtBio.setForeground(Color.white);
        if (comunidade.getArtista().getNomeArtistico() != null) {
            txtBio.setText(comunidade.getArtista().getBio());
        }

        JScrollPane scrollPanePublicar = new JScrollPane(txtBio);
        scrollPanePublicar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPanePublicar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanePublicar.setPreferredSize(new Dimension(400, 70));
        scrollPanePublicar.setViewportView(txtBio);

        holderBioEditar.add(scrollPanePublicar);

        this.add(holderBioEditar);
        //fim bio editar=========================================================

        JPanel margemPosBioPanel = new JPanel();
        margemPosBioPanel.setPreferredSize(new Dimension(0, 15));
        margemPosBioPanel.setMaximumSize(margemPosBioPanel.getPreferredSize());
        margemPosBioPanel.setOpaque(false);
        this.add(margemPosBioPanel);

        //background editar=======================================================
        JPanel holderBackgroundEditar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        holderBackgroundEditar.setPreferredSize(new Dimension(this.getWidth(), 80));
        holderBackgroundEditar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        holderBackgroundEditar.setOpaque(false);

        JPanel margemBackgroundPanel = new JPanel();
        margemBackgroundPanel.setPreferredSize(new Dimension(100, 0));

        JLabel lblBackgroundCorpo = new JLabel("Background: ");
        lblBackgroundCorpo.setForeground(Color.white);
        lblBackgroundCorpo.setFont(new Font("Arial", Font.BOLD, 12));
        holderBackgroundEditar.add(lblBackgroundCorpo);

        backgroundPreviewPanel = new JPanelCustomizado(comunidade.getFotoBackground(), null);
        backgroundPreviewPanel.setPreferredSize(new Dimension(80, 80));
        holderBackgroundEditar.add(backgroundPreviewPanel);

        JButton btnBackgroundEditar = new JButton("Substituir imagem");
        btnBackgroundEditar.addActionListener((e) -> {
            substituirBackgroundClicado(e);
        });
        holderBackgroundEditar.add(btnBackgroundEditar);

        holderBackgroundEditar.add(margemBackgroundPanel);
        this.add(holderBackgroundEditar);

        //FIM EDITAR BACKGROUND-------------------------
        //margem pos background
        JPanel margemPosBackgroundPanel = new JPanel();
        margemPosBackgroundPanel.setPreferredSize(new Dimension(0, 15));
        margemPosBackgroundPanel.setMaximumSize(margemPosBioPanel.getPreferredSize());
        margemPosBackgroundPanel.setOpaque(false);
        this.add(margemPosBackgroundPanel);

        //EDITAR FOTO PERFIL======================================================
        JPanel holderFotoEditar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        holderFotoEditar.setPreferredSize(new Dimension(this.getWidth(), 80));
        holderFotoEditar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        holderFotoEditar.setOpaque(false);

        JPanel margemFotoPanel = new JPanel();
        margemFotoPanel.setPreferredSize(new Dimension(100, 0));

        JLabel lblFotoCorpo = new JLabel("Foto de Perfil: ");
        lblFotoCorpo.setForeground(Color.white);
        lblFotoCorpo.setFont(new Font("Arial", Font.BOLD, 12));
        holderFotoEditar.add(lblFotoCorpo);

        fotoPreviewPanel = new JPanelCustomizado(comunidade.getArtista().getFotoPerfil(), null);
        fotoPreviewPanel.setPreferredSize(new Dimension(80, 80));
        holderFotoEditar.add(fotoPreviewPanel);

        JButton btnFotoEditar = new JButton("Substituir imagem");
        btnFotoEditar.addActionListener((e) -> {
            substituirFotoClicado(e);
        });
        holderFotoEditar.add(btnFotoEditar);

        holderFotoEditar.add(margemFotoPanel);
        this.add(holderFotoEditar);
        // FIM FOTO EDITAR -----------------------------------
        //margem pós foto perfil editar
        JPanel margemPosFotoPanel = new JPanel();
        margemPosFotoPanel.setPreferredSize(new Dimension(0, 15));
        margemPosFotoPanel.setMaximumSize(margemPosBioPanel.getPreferredSize());
        margemPosFotoPanel.setOpaque(false);
        this.add(margemPosFotoPanel);

        //COR FUNDO POST EDITAR (rgb)===============================
        JPanel holderCorFundoPostEditar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        holderCorFundoPostEditar.setPreferredSize(new Dimension(this.getWidth(), 50));
        holderCorFundoPostEditar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        holderCorFundoPostEditar.setOpaque(false);

        JLabel lblCorDoPost = new JLabel("Cor do fundo do post (#000000): ");
        lblCorDoPost.setForeground(Color.white);
        lblCorDoPost.setFont(new Font("Arial", Font.BOLD, 12));
        holderCorFundoPostEditar.add(lblCorDoPost);

        txtNovaCorFundoPost = new JTextField(formataEmHexComSustenido(comunidade.getCorFundoPost()));
        txtNovaCorFundoPost.setPreferredSize(new Dimension(400, 25));
        txtNovaCorFundoPost.setBackground(new Color(80, 80, 80));
        txtNovaCorFundoPost.setForeground(Color.white);
        txtNovaCorFundoPost.setBorder(new EmptyBorder(0, 0, 0, 0));
        holderCorFundoPostEditar.add(txtNovaCorFundoPost);

        this.add(holderCorFundoPostEditar);

        //fim cor fundo post editar------------------------------
        //margem pós cor fundo post
        JPanel margemPosCorPostPanel = new JPanel();
        margemPosCorPostPanel.setPreferredSize(new Dimension(0, 15));
        margemPosCorPostPanel.setMaximumSize(margemPosBioPanel.getPreferredSize());
        margemPosCorPostPanel.setOpaque(false);
        this.add(margemPosCorPostPanel);

        //COR FONTE POST EDITAR (rgb)=========================
        JPanel holderCorFontePostEditar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        holderCorFontePostEditar.setPreferredSize(new Dimension(this.getWidth(), 50));
        holderCorFontePostEditar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        holderCorFontePostEditar.setOpaque(false);

        JLabel lblCorDaFonteDoPost = new JLabel("Cor da Fonte do post (#FFFFFF): ");
        lblCorDaFonteDoPost.setForeground(Color.white);
        lblCorDaFonteDoPost.setFont(new Font("Arial", Font.BOLD, 12));
        holderCorFontePostEditar.add(lblCorDaFonteDoPost);

        txtNovaCorFontePost = new JTextField(formataEmHexComSustenido(comunidade.getCorFontePost()));
        txtNovaCorFontePost.setPreferredSize(new Dimension(400, 25));
        txtNovaCorFontePost.setBackground(new Color(80, 80, 80));
        txtNovaCorFontePost.setForeground(Color.white);
        txtNovaCorFontePost.setBorder(new EmptyBorder(0, 0, 0, 0));
        holderCorFontePostEditar.add(txtNovaCorFontePost);

        this.add(holderCorFontePostEditar);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener((e) -> {
            btnSalvarClicado(e);
        });
        this.add(btnSalvar);

        //fim cor fonte post editar-----------------------------
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaEditarComunidade tEC = new TelaEditarComunidade(null);
            tEC.setVisible(true);
        });
    }

    public void lblSairClicado(MouseEvent e) {
        System.out.println("Label sair");
        TelaInicial ti = new TelaInicial();
        ti.setVisible(true);
        dispose();
    }

    public void btnVoltarClicado(ActionEvent e) {
        System.out.println("botão Voltar tela comunidades clicado");
        TelaComunidade tc = new TelaComunidade(comunidade);
        tc.setLocationRelativeTo(null);
        tc.setVisible(true);
        dispose();
    }

    public void substituirBackgroundClicado(ActionEvent e) {
        System.out.println("Substituir background clicado");
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooserImagem.addChoosableFileFilter(imageFilter);
        if (fileChooserImagem.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            comunidade.setFotoBackground(new Imagem(fileChooserImagem.getSelectedFile()).getImagem());
            backgroundPreviewPanel.setBackgroundImage(comunidade.getFotoBackground());
            backgroundPreviewPanel.repaint();
        }
    }

    public void substituirFotoClicado(ActionEvent e) {
        System.out.println("Substituir foto perfil clicado");
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooserImagem.addChoosableFileFilter(imageFilter);
        if (fileChooserImagem.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            comunidade.getArtista().setFotoPerfil(new Imagem(fileChooserImagem.getSelectedFile()).getImagem());
            fotoPreviewPanel.setBackgroundImage(comunidade.getArtista().getFotoPerfil());
            fotoPreviewPanel.repaint();
        }
    }

    public void btnSalvarClicado(ActionEvent e) {
        System.out.println("botão salvar clicado");

        if (!txtNovoNome.getText().isBlank() && txtNovoNome.getText().length() <= 40) {
            if (!txtBio.getText().isBlank() && txtBio.getText().length() <= 50) {
                if (estaFormatadoEmHex(txtNovaCorFontePost.getText())) {
                    if (estaFormatadoEmHex(txtNovaCorFundoPost.getText())) {
                        System.out.println("Todos os valores estão corretos, atribuindo dados ao objeto comunidade");
                        System.out.println("bio pre update: " + txtBio.getText());
                        Artista artistaUpdate = new Artista(comunidade.getArtista().getCodUsuario(),
                                comunidade.getArtista().getNomeUsuario(),
                                comunidade.getArtista().getEmail(),
                                comunidade.getArtista().getSenha(),
                                txtNovoNome.getText(),
                                comunidade.getArtista().getFotoPerfil(),
                                txtBio.getText());
                        System.out.println("bio do artista pre update: " + txtBio.getText());
                        Comunidade comunidadeUpdate = new Comunidade(comunidade.getIdComunidade(),
                                artistaUpdate,
                                comunidade.getFotoBackground(),
                                formatarEmHex0x(txtNovaCorFundoPost.getText()),
                                formatarEmHex0x(txtNovaCorFontePost.getText()));
                        comunidade.getArtista().setNomeArtistico(txtNovoNome.getText());
                        comunidade.getArtista().setBio(txtBio.getText());

                        System.out.println("cor fonte: " + comunidadeUpdate.getCorFontePost());

                        System.out.println("cor fundo: " + comunidadeUpdate.getCorFundoPost());
                        if (Principal.ccont.editarComunidade(comunidadeUpdate)) {
                            JOptionPane.showMessageDialog(rootPane, "Comunidade editada com sucesso!");
                            TelaComunidade telaComunidade = new TelaComunidade(Principal.ccont.getComunidadePeloArtistaId(Principal.ccont.getUsuarioLogado().getCodUsuario()));
                            telaComunidade.setVisible(true);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(rootPane, "Erro ao editar comunidade :(");
                        }
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Preecha o campo nova cor fundo com valor hexadecimal. Exemplo: #FFFFFF");
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Preecha o campo nova cor fonte com valor hexadecimal. Exemplo: #FFFFFF");
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Preecha o campo bio corretamente: não deixar em branco, limite de caracteres: 50.");

            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Preecha o campo nome corretamente: não deixar em branco, limite de caracteres: 40.");
        }

        comunidade.getArtista().setNomeArtistico(txtNovoNome.getText());
    }

    //classes privadas
    private class RoundedImagePanel extends JPanel {

        private BufferedImage image;

        public RoundedImagePanel(String imagePath, Dimension prefSize, Dimension minSize, Dimension MaxSize) {
            try {
                image = ImageIO.read(new File(imagePath)); // Carrega a imagem
            } catch (IOException e) {
                e.printStackTrace();
            }
            setPreferredSize(prefSize);
            setMinimumSize(minSize);
            setMaximumSize(MaxSize);
            setOpaque(false);
        }

        public RoundedImagePanel(byte[] imageByteArray, Dimension prefSize, Dimension minSize, Dimension MaxSize) {
            try {
                image = ImageIO.read(new ByteArrayInputStream(imageByteArray)); // Carrega a imagem
            } catch (IOException e) {
                e.printStackTrace();
            }
            setPreferredSize(prefSize);
            setMinimumSize(minSize);
            setMaximumSize(MaxSize);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Cria uma forma arredondada
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setClip(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 200, 200)); // Bordas arredondadas, o 100 é quão arredondado vai ficar

            // Desenha a imagem
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this); // Desenha a imagem preenchendo o painel
        }
    }

    private class JPanelCustomizado extends JPanel {

        private Image backgroundImage;
        private int heightFixo;
        private int widthFixo;
        private Component c;

        public JPanelCustomizado(String imagePath, Component c) {
            // Carrega a imagem usando o caminho fornecido
            backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
            widthFixo = 0;
            heightFixo = 0;
            this.c = c;
        }

        public JPanelCustomizado(byte[] byteImage, Component c) {
            // Carrega a imagem usando o caminho fornecido
            backgroundImage = new Imagem(byteImage).convertByteArrayToImage(byteImage);
            widthFixo = 0;
            heightFixo = 0;
            this.c = c;
        }

        public void setBackgroundImage(byte[] byteImage) {
            backgroundImage = new Imagem(byteImage).convertByteArrayToImage(byteImage);
            widthFixo = 0;
            heightFixo = 0;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (backgroundImage != null) {
                // Obtém a largura e altura da imagem
                int imageWidth = backgroundImage.getWidth(null);
                int imageHeight = backgroundImage.getHeight(null);

                // Calcula a nova largura e altura da imagem mantendo a proporção
                int width = getWidth(); // O painel tem a largura que queremos para a imagem
                int height = (int) ((double) imageHeight / imageWidth * width); // Mantém a proporção

                // Se a altura calculada for maior que o painel, usamos a altura original da imagem
                if (height > getHeight()) {
                    height = getHeight();
                    width = (int) ((double) imageWidth / imageHeight * height);
                }

                int x;
                int y;
                if (c instanceof JScrollPane jScrollPane) {
                    // Obtém a posição de rolagem atual
                    JScrollBar verticalScrollBar = jScrollPane.getVerticalScrollBar();
                    int scrollPos = verticalScrollBar.getValue();

                    // Desenha a imagem de fundo, ajustada conforme a posição do scroll
                    x = (getWidth() - width) / 2;
                    y = scrollPos % getHeight(); // Faz com que a imagem acompanhe o scroll vertical
                } else {
                    x = (getWidth() - width) / 2;
                    y = (getHeight() - height) / 2;
                }

                g.drawImage(backgroundImage, x, y, width, height, this);
            }
        }
    }

    private boolean estaFormatadoEmHex(String texto) {
        char[] textoChar = texto.toCharArray();
        System.out.println(texto);
        boolean res = true;
        if (textoChar.length == 7) {
            if (textoChar[0] == '#') {
                for (int i = 1; i < (textoChar.length); i++) {
                    System.out.print(textoChar[i]);
                    switch (textoChar[i]) {
                        case 'A':
                            res = true;
                            break;
                        case 'B':
                            res = true;
                            break;
                        case 'C':
                            res = true;
                            break;
                        case 'D':
                            res = true;
                            break;
                        case 'E':
                            res = true;
                            break;
                        case 'F':
                            res = true;
                            break;
                        case '0':
                            res = true;
                            break;
                        case '1':
                            res = true;
                            break;
                        case '2':
                            res = true;
                            break;
                        case '3':
                            res = true;
                            break;
                        case '4':
                            res = true;
                            break;
                        case '5':
                            res = true;
                            break;
                        case '6':
                            res = true;
                            break;
                        case '7':
                            res = true;
                            break;
                        case '8':
                            res = true;
                            break;
                        case '9':
                            res = true;
                            break;
                        default:
                            res = false;
                            break;
                    }
                    if (!res) {
                        break;
                    }
                }
            } else {
                res = false;
            }
        } else {
            res = false;
        }
        System.out.println(" formatado em hex: " + res);
        return res;
    }

    private String formatarEmHex0x(String hexNormal) {
        char hex0x[] = new char[8];
        hex0x[0] = '0';
        hex0x[1] = 'x';
        //começa no 1 para pular o #
        for (int i = 1; i < hexNormal.length(); i++) {
            //adiciona +1 no index de hex0x pois ele tem dois caracteres antes de começar(0x)
            //enquanto o hexNormal tem apenas um(#)
            hex0x[i + 1] = hexNormal.toCharArray()[i];
        }
        return String.valueOf(hex0x).toUpperCase();
    }

    private String formataEmHexComSustenido(String hex0x) {
        char[] hexNormal = new char[7];
        hexNormal[0] = '#';
        for (int i = 0; i < hex0x.length(); i++) {
            if (i > 1) {
                hexNormal[i - 1] = hex0x.toCharArray()[i];
            }
        }
        return String.valueOf(hexNormal).toUpperCase();
    }
}
