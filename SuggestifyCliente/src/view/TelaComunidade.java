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
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import modelDominio.Comunidade;
import modelDominio.Publicacao;
import modelDominio.PublicacaoComum;
import view.util.Imagem;

public class TelaComunidade extends JFrame {

    private JPanelCustomizado feedPanel;
    //private JPanel feedPanel;
    private JPanel centerContainer;
    private ArrayList<PublicacaoComum> posts;
    private Comunidade comunidade;
    private JFileChooser fileChooserImagem = new javax.swing.JFileChooser();
    private Imagem imagemParaPublicar;
    private JLabel lblImagem;
    private JTextArea textoPublicar;
    private JScrollPane bodyScrollPane;
    JButton btnConfig, btnAlbuns, btnMusicas, btnTags, btnEditar;
    JLabel lblSair;
    private Color corFontePublicacao, corFundoPublicacao;

    public TelaComunidade(Comunidade comunidade) {
        imagemParaPublicar = null;
        this.comunidade = comunidade;
        int[] corFontePublicacao, corFundoPublicacao;
        //descobrindo o RGB de cada hex
        corFontePublicacao = converterHexParaRgb(this.comunidade.getCorFontePost());
        corFundoPublicacao = converterHexParaRgb(this.comunidade.getCorFundoPost());
        //monta o Color com o rgb das cores da comunidade
        this.corFontePublicacao = new Color(corFontePublicacao[0], corFontePublicacao[1], corFontePublicacao[2]);
        this.corFundoPublicacao = new Color(corFundoPublicacao[0], corFundoPublicacao[1], corFundoPublicacao[2]);
        this.setTitle("Comunidade");
        this.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setIconImage(new ImageIcon(getClass().getResource("/view/images/logoRoxoComNome.png")).getImage());
        this.getContentPane().setBackground(new Color(30, 30, 30));
        this.setMinimumSize(new Dimension(900, 500));

        //------------ HEADER----------------
        //jPanel do header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0x5755DF));
        //x,y,width, height
        headerPanel.setPreferredSize(new Dimension(900, 25));
        headerPanel.setLayout(new BorderLayout());
        this.add(headerPanel, BorderLayout.NORTH);

        //botao de configuracoes do header
        btnConfig = new JButton();
        btnConfig.setPreferredSize(new Dimension(25, 25));
        btnConfig.addActionListener((ActionEvent e) -> {
            btnConfigClicado(e);
        });
        headerPanel.add(btnConfig, BorderLayout.WEST);

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

        //-----------------CORPO ------------------------
        //JScrollPane para o corpo
        bodyScrollPane = new JScrollPane();
        // Configuração para bodyScrollPane
        bodyScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        bodyScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        bodyScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        //bodyScrollPane.setPreferredSize(new Dimension(800,450));

        //contentPanel com background
        //ATENÇÃO: TODOS OS ELEMENTOS DO FEED DEVEM SER ADICIONADOS A feedPanel
        //JPanel holderFeedPanel = new JPanelCustomizado(comunidade.getFotoBackground());
        feedPanel = new JPanelCustomizado(comunidade.getFotoBackground(), bodyScrollPane);
        //feedPanel = new JPanel();
//        holderFeedPanel.add(feedPanel);
//        holderFeedPanel.setOpaque(false);
        //feedPanel = new JPanel();
        feedPanel.setOpaque(false);

        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));

        feedPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        // ARRUMAR ISSOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
        this.add(bodyScrollPane, BorderLayout.CENTER);
        //espaço fixo entre header e menu
        Dimension espaçamentoMenu = new Dimension(0, 150);
        feedPanel.add(Box.createRigidArea(espaçamentoMenu));

        //LAYERED PANE PARA COLOCAR MENU E PROFILE PICTURE DO ARTISTA
        JLayeredPane menuEPfpLayeredPane = new JLayeredPane();
        menuEPfpLayeredPane.setPreferredSize(new Dimension(900, 250));
        //menuEPfpLayeredPane.setBackground(Color.red);
        menuEPfpLayeredPane.setOpaque(false);
        menuEPfpLayeredPane.setMinimumSize(new Dimension(900, 250));
        //MENU PANEL ---------------------
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(new Color(0x5755DF));
        menuPanel.setPreferredSize(new Dimension(900, 50));
        menuPanel.setMinimumSize(new Dimension(900, 50));
        menuPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));//utiliza-se o maxValue para a tela se redimensionar

        //panel para botao a esquerda
        JPanel panelBotaoAEsquerda = new JPanel();
        panelBotaoAEsquerda.setLayout(new BoxLayout(panelBotaoAEsquerda, BoxLayout.X_AXIS));
        panelBotaoAEsquerda.setOpaque(false);
        menuPanel.add(panelBotaoAEsquerda, BorderLayout.WEST);

        btnAlbuns = new JButton("Gerenciar álbuns");
        btnAlbuns.setBackground(new Color(0x2C2C2C));
        btnAlbuns.setFont(new Font("Arial", Font.BOLD, 9));

        btnAlbuns.setPreferredSize(new Dimension(120, 25));
        btnAlbuns.setMaximumSize(new Dimension(120, 25));
        btnAlbuns.setForeground(Color.white);
        btnAlbuns.addActionListener((ActionEvent e) -> {
            btnGerenciarAlbunsClicado(e);
        });

        btnAlbuns.setBorderPainted(false);
        panelBotaoAEsquerda.add(Box.createRigidArea(new Dimension(20, HEIGHT)));
        panelBotaoAEsquerda.add(btnAlbuns);

        //panel para organizar os botões a direita
        JPanel panelBotoesADireita = new JPanel();
        panelBotoesADireita.setLayout(new BoxLayout(panelBotoesADireita, BoxLayout.X_AXIS));
        panelBotoesADireita.setOpaque(false);//transparente
        menuPanel.add(panelBotoesADireita, BorderLayout.EAST);

        btnMusicas = new JButton("Gerenciar músicas");
        btnMusicas.setBackground(new Color(0x2C2C2C));
        btnMusicas.setFont(new Font("Arial", Font.BOLD, 9));
        btnMusicas.setPreferredSize(new Dimension(100, 50));
        btnMusicas.setForeground(Color.white);
        btnMusicas.addActionListener((ActionEvent e) -> {
            btnGerenciarMusicasClicado(e);
        });
        btnMusicas.setBorderPainted(false);
        panelBotoesADireita.add(btnMusicas);
        panelBotoesADireita.add(Box.createRigidArea(new Dimension(20, 0)));

        btnTags = new JButton("Gerenciar tags");
        btnTags.setBackground(new Color(0x2C2C2C));
        btnTags.setForeground(Color.white);
        btnTags.setFont(new Font("Arial", Font.BOLD, 9));
        btnTags.setPreferredSize(new Dimension(100, 50));
        btnTags.addActionListener((ActionEvent e) -> {
            btnGerenciarTagsClicado(e);
        });
        btnTags.setBorderPainted(false);
        panelBotoesADireita.add(btnTags);
        panelBotoesADireita.add(Box.createRigidArea(new Dimension(20, 0)));

        btnEditar = new JButton("Editar");
        btnEditar.setBackground(new Color(0x2C2C2C));
        btnEditar.setForeground(Color.white);
        btnEditar.setFont(new Font("Arial", Font.BOLD, 9));
        btnEditar.setPreferredSize(new Dimension(100, 50));
        btnEditar.addActionListener((ActionEvent e) -> {
            btnEditarClicado(e);
        });

        btnEditar.setBorderPainted(false);
        panelBotoesADireita.add(btnEditar);
        menuEPfpLayeredPane.add(menuPanel, JLayeredPane.DEFAULT_LAYER);//adicionando o menu ao Layer

        //FOTO ARTISTA/COMUNIDADE
        RoundedImagePanel pfpPanel = new RoundedImagePanel(comunidade.getArtista().getFotoPerfil(),
                new Dimension(150, 150), new Dimension(80, 80), new Dimension(250, 250));
        //        preferred                 min                         max
        menuEPfpLayeredPane.add(pfpPanel, JLayeredPane.PALETTE_LAYER); //camada acima da normal

        //utilizado para resimensionar os elementos de menuEPfpLayeredPane
        //toda vez que ele for redimensionado
        menuEPfpLayeredPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                // Calcula a posição centralizada para o pfpPanel
                int paneWidth = menuEPfpLayeredPane.getWidth();
                int paneHeight = menuEPfpLayeredPane.getHeight();

                // Tamanho desejado para o pfpPanel
                int pfpWidth = 150;
                int pfpHeight = 150;

                //tamanho desejado para menuPanel
                //int menuPanelWidth = paneWidth;
                int menuPanelHeight = 50;

                // Calcula a posição centralizada
                int xPfp = (paneWidth - pfpWidth) / 2;
                int yPfp = (paneHeight - pfpHeight) / 2;
                int xMenuPanel = 0;
                int yMenuPanel = (paneHeight - menuPanelHeight) / 2;

                // Ajusta a posição e o tamanho do pfpPanel
                pfpPanel.setBounds(xPfp, yPfp, pfpWidth, pfpHeight);

                menuPanel.setBounds(xMenuPanel, yMenuPanel, paneWidth, menuPanelHeight); // Mantém a altura fixa
                //menuPanel.setSize(paneWidth, 50);
            }
        });

        //feedPanel.add(menuPanel);
        //Panel para organizar os espaços e evitar que o menu seja comprimido
        feedPanel.add(menuEPfpLayeredPane);
        feedPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        //feedPanel.add(Box.createVerticalStrut(100));

        // LISTA DE POSTS
        centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        centerContainer.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        //centerContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        centerContainer.setOpaque(false);
        //centerContainer.setBackground(Color.cyan);

        centerContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        recarregarPosts();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaComunidade comunidade = new TelaComunidade(null);
            comunidade.setVisible(true);
        });
    }

    public void lblSairClicado(MouseEvent e) {
        System.out.println("Label sair");
        TelaInicial ti = new TelaInicial();
        ti.setVisible(true);
        dispose();
    }

    public void btnConfigClicado(ActionEvent e) {
        System.out.println("Config");
        TelaConfiguracoes tc = new TelaConfiguracoes(this.comunidade);
        tc.setLocationRelativeTo(null);
        tc.setVisible(true);
        dispose();
    }

    public void btnGerenciarAlbunsClicado(ActionEvent e) {
        System.out.println("Gerenciar álbuns");
        TelaGerenciamentoAlbumMusica tgam = new TelaGerenciamentoAlbumMusica(this.comunidade, 2);
        tgam.setLocationRelativeTo(null);
        tgam.setVisible(true);
        dispose();
    }

    public void btnGerenciarMusicasClicado(ActionEvent e) {
        System.out.println("Gerenciar músicas");
        TelaGerenciamentoAlbumMusica tgam = new TelaGerenciamentoAlbumMusica(this.comunidade, 1);
        tgam.setLocationRelativeTo(null);
        tgam.setVisible(true);
        dispose();
    }

    public void btnGerenciarTagsClicado(ActionEvent e) {
        System.out.println("Gerenciar tags");
        TelaMenuTags menuTags = new TelaMenuTags(this.comunidade);
        menuTags.setLocationRelativeTo(null);
        menuTags.setVisible(true);
        dispose();
    }

    public void btnEditarClicado(ActionEvent e) {
        System.out.println("Botao Editar clicado");
        TelaEditarComunidade tEC = new TelaEditarComunidade(comunidade);
        tEC.setLocationRelativeTo(null);
        tEC.setVisible(true);
        this.dispose();
    }

    public void btnPublicarClicado(ActionEvent e) {
        System.out.println("Publicar clicado");
        //verificando tamanho do texto
        if (textoPublicar.getText().length() <= 200) {
            PublicacaoComum pComum;
            if (textoPublicar.getText().equals("") && imagemParaPublicar == null) {

                JOptionPane.showMessageDialog(rootPane, "Não é possível publicar sem ter pelo menos texto ou imagem!");

            } else {

                if (imagemParaPublicar == null) {
                    pComum = new PublicacaoComum(comunidade.getArtista(),
                            textoPublicar.getText(),
                            0);
                } else {
                    pComum = new PublicacaoComum(comunidade.getArtista(),
                            textoPublicar.getText(),
                            0,
                            imagemParaPublicar.getImagem());
                }
                boolean resultadoPost = Principal.ccont.publicarComum(pComum, comunidade);
                if (resultadoPost) {
                    recarregarPosts();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Erro ao publicar!");
                }

            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Excedido o número de caracteres(200)");
        }

    }

    public void btnUparImagemClicado(ActionEvent e) {
        System.out.println("Carregar imagem clicado");
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooserImagem.addChoosableFileFilter(imageFilter);
        if (fileChooserImagem.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            imagemParaPublicar = new Imagem(fileChooserImagem.getSelectedFile());
            lblImagem.setIcon(imagemParaPublicar.getImageIcon());
        }
    }

    //inner class de tipo JPanel para possibilitar colocar imagens como backgound
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

    private class PostPanel extends JPanel {

        JLabel lblLikes;
        JButton btnCurtir;

        public PostPanel(PublicacaoComum publicacaoComum) {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(corFundoPublicacao);

            setMaximumSize(new Dimension(400, 450));
            // Painel superior com foto de usuário e nome
            JPanel panelSuperior = new JPanel();
            panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.X_AXIS));
            panelSuperior.setOpaque(false);

            // Foto de usuário
            RoundedImagePanel panelFotoUsuario = new RoundedImagePanel(publicacaoComum.getUsuario().getFotoPerfil(),
                    new Dimension(75, 75), new Dimension(75, 75), new Dimension(75, 75));
            panelSuperior.add(panelFotoUsuario);

            // Painel para organizar nome e texto
            JPanel panelNomeETexto = new JPanel();
            panelNomeETexto.setLayout(new BoxLayout(panelNomeETexto, BoxLayout.Y_AXIS));
            panelNomeETexto.setOpaque(false);
            panelNomeETexto.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelSuperior.add(Box.createRigidArea(new Dimension(10, 0))); // Espaço entre imagem e texto
            panelSuperior.add(panelNomeETexto);

            // Nome do usuário
            JLabel usernameLabel = new JLabel(publicacaoComum.getUsuario().getNomeUsuario());
            usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
            usernameLabel.setForeground(corFontePublicacao);
            usernameLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
            panelNomeETexto.add(usernameLabel);

            // Mensagem do post
            JTextArea postTextArea = new JTextArea(publicacaoComum.getTexto());
            postTextArea.setLineWrap(true);
            postTextArea.setWrapStyleWord(true);
            postTextArea.setEditable(false);
            postTextArea.setOpaque(false);
            postTextArea.setAlignmentX(JPanel.LEFT_ALIGNMENT);
            postTextArea.setForeground(corFontePublicacao);
            panelNomeETexto.add(postTextArea);

            add(panelSuperior, BorderLayout.NORTH);

            // Imagem do post, se houver
            if (publicacaoComum.getImagem() != null) {
                setPreferredSize(new Dimension(400, 450)); // Defina uma altura adequada para os posts
                Imagem imagemPost = new Imagem(publicacaoComum.getImagem());

                JPanelCustomizado imageLabel = new JPanelCustomizado(imagemPost.getImagem(), this);
                imageLabel.setOpaque(false);
                imageLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

                add(imageLabel, BorderLayout.CENTER);
            }

            JPanel holderInteracoesPost = new JPanel();
            holderInteracoesPost.setOpaque(false);
            holderInteracoesPost.setPreferredSize(new Dimension(400, 100));
            holderInteracoesPost.setLayout(new BoxLayout(holderInteracoesPost, BoxLayout.X_AXIS));

            btnCurtir = new JButton("Like");

            if (Principal.ccont.usuarioJaCurtiu(publicacaoComum, Principal.ccont.getUsuarioLogado())) {
                btnCurtir.setBackground(new Color(0x5755DF));
                btnCurtir.setForeground(Color.white);
            } else {
                btnCurtir.setBackground(Color.white);
                btnCurtir.setForeground(Color.black);
            }

            btnCurtir.addActionListener((e) -> {
                btnCurtirClicado(e, publicacaoComum);
            });
            holderInteracoesPost.add(btnCurtir);

            lblLikes = new JLabel(String.valueOf(publicacaoComum.getLikes()));
            holderInteracoesPost.add(lblLikes);

//            JButton btnComentarios = new JButton("Comentarios");
//            btnComentarios.setBackground(Color.white);
//            btnComentarios.setForeground(Color.black);
//            btnComentarios.addActionListener((e) -> {
//                btnComentarioClicado(e, publicacaoComum);
//            });
//            holderInteracoesPost.add(btnComentarios);

            add(holderInteracoesPost, BorderLayout.SOUTH);
        }

        public void btnCurtirClicado(ActionEvent e, Publicacao p) {

            //FAZER SISTEMA PARA DETECTAR SE USUARIO JÁ CURTIU POST---------------------------------------====================================================
            if (Principal.ccont.usuarioJaCurtiu(p, Principal.ccont.getUsuarioLogado())) {
                //se já curtiu, então descurtir
                System.out.println("botão curtir clicado - descurtir");
                if (Principal.ccont.descurtirPublicacaoOuComentario(p, Principal.ccont.getUsuarioLogado())) {
                    //sucesso ao descurtir
                    btnCurtir.setBackground(Color.white);
                    btnCurtir.setForeground(Color.black);
                    p.setLikes(p.getLikes() - 1);
                    lblLikes.setText(String.valueOf(p.getLikes()));

                } else {
                    JOptionPane.showMessageDialog(rootPane, "Erro ao descurtir post");
                }

            } else {
                //se não curtiu, então curtir
                System.out.println("botão curtir clicado - curtir");
                if (Principal.ccont.curtirPublicacaoOuComentario(p, Principal.ccont.getUsuarioLogado())) {
                    //sucesso ao curtir
                    btnCurtir.setBackground(new Color(0x5755DF));
                    btnCurtir.setForeground(Color.white);
                    p.setLikes(p.getLikes() + 1);
                    lblLikes.setText(String.valueOf(p.getLikes()));
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Erro ao curtir post");
                }
            }

        }

    }

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

    public void recarregarPosts() {
        centerContainer.removeAll();
        JLabel nomeComunidade = new JLabel(this.comunidade.getArtista().getNomeArtistico());
        nomeComunidade.setForeground(corFundoPublicacao);
        nomeComunidade.setFont(new Font("Miriam Libre", Font.BOLD, 28));
        centerContainer.add(nomeComunidade);
        centerContainer.add(Box.createRigidArea(new Dimension(0, 8)));
        System.out.println("bio na variavel: " + this.comunidade.getArtista().getBio());
        JLabel bio = new JLabel(this.comunidade.getArtista().getBio());
        bio.setForeground(corFundoPublicacao);
        bio.setFont(new Font("Miriam Libre", Font.BOLD, 14));
        centerContainer.add(bio);
        centerContainer.add(Box.createRigidArea(new Dimension(0, 8)));

        nomeComunidade.setAlignmentX(CENTER_ALIGNMENT);
        bio.setAlignmentX(CENTER_ALIGNMENT);
        System.out.println("bio: " + bio.getText());
        // LISTA DE POSTS
        posts = Principal.ccont.carregaPostsComunidade(comunidade);
        centerContainer.add(Box.createVerticalGlue());
        centerContainer.add(Box.createRigidArea(new Dimension(0, 10))); // Espaço entre os posts

        //ESPAÇO PARA O USUARIO LOGADO PUBLICAR-----------------------------------------------------------------
        //holder
        JPanel holderPublicar = new JPanel();
        holderPublicar.setLayout(new BoxLayout(holderPublicar, BoxLayout.Y_AXIS));
        holderPublicar.setPreferredSize(new Dimension(600, 350));
        holderPublicar.setBackground(corFundoPublicacao);
        holderPublicar.setAlignmentX(Component.CENTER_ALIGNMENT);
        //texto apenas para indicar para o uusário o que ele pode fazer (publicar)
        JLabel lblPublicar = new JLabel("Publicar...");
        lblPublicar.setForeground(corFontePublicacao);
        lblPublicar.setAlignmentX(Component.LEFT_ALIGNMENT);
        holderPublicar.add(lblPublicar);

        //textArea para o usuario digitar o texto do post
        textoPublicar = new JTextArea();
        textoPublicar.setWrapStyleWord(true);
        textoPublicar.setPreferredSize(new Dimension(400, 500));
        textoPublicar.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
        textoPublicar.setAutoscrolls(true);
        textoPublicar.setLineWrap(true);
        textoPublicar.setBackground(Color.lightGray);
        textoPublicar.setForeground(corFontePublicacao);

        JPanel alinarTextArea = new JPanel(new FlowLayout());
        alinarTextArea.setOpaque(false);

        //CRIAR LIMITE DE CARACTERES CONFORME O BANCO.(fazer essa verificação no botão PUBLICAR)-----------------
        JScrollPane scrollPanePublicar = new JScrollPane(textoPublicar);
        scrollPanePublicar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPanePublicar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanePublicar.setPreferredSize(new Dimension(400, 350));
        //scrollPanePublicar.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        scrollPanePublicar.setViewportView(textoPublicar);
        alinarTextArea.add(scrollPanePublicar);
        holderPublicar.add(alinarTextArea);

        //USAR NO POST------------===================================================================================
        JPanel boxAlinharLikeEComentario = new JPanel();
        boxAlinharLikeEComentario.setLayout(new BoxLayout(boxAlinharLikeEComentario, BoxLayout.X_AXIS));
        boxAlinharLikeEComentario.setOpaque(false);
        holderPublicar.add(Box.createRigidArea(new Dimension(0, 10)));
        //===========================================================================================================

        //HOLDER E MAIS HOLDERS PARA COLOCAR ADEQUADAMENTE BOTÕES DE PUBLICAR E CARREGAR IMAGEM
        JPanel jPanelCentralizarBotoes = new JPanel(new BorderLayout());
        jPanelCentralizarBotoes.setOpaque(false);
        JPanel espacamentoBotoes = new JPanel();
        JPanel espacamentoBotoes2 = new JPanel();
//      espacamentoBotoes.setBackground(Color.red);
//      espacamentoBotoes2.setBackground(Color.red);
        espacamentoBotoes.setPreferredSize(new Dimension(99, 50));
        espacamentoBotoes2.setPreferredSize(new Dimension(99, 50));
        espacamentoBotoes.setOpaque(false);
        espacamentoBotoes2.setOpaque(false);
        jPanelCentralizarBotoes.add(espacamentoBotoes, BorderLayout.EAST);
        jPanelCentralizarBotoes.add(espacamentoBotoes2, BorderLayout.WEST);

        JPanel holderBotoesPublicar = new JPanel(new BorderLayout());
        holderBotoesPublicar.setOpaque(false);
        //BOTAO POSTAR/PUBLICAR
        JButton btnPublicar = new JButton("Postar");
        btnPublicar.addActionListener((ActionEvent e) -> {
            btnPublicarClicado(e);
        });
        //BOTAO CARREGAR IMAGEM
        JButton btnUparImagem = new JButton("Carregar imagem");
        btnUparImagem.addActionListener((ActionEvent e) -> {
            btnUparImagemClicado(e);
        });

        lblImagem = new JLabel();
        lblImagem.setOpaque(false);
        lblImagem.setBackground(Color.DARK_GRAY);
        lblImagem.setSize(new Dimension(50, 50));
        holderBotoesPublicar.add(btnPublicar, BorderLayout.EAST);

        JPanel holderLblImagem = new JPanel(new BorderLayout());
        holderLblImagem.setOpaque(false);

        JPanel espacamentoImagemW = new JPanel();
        espacamentoImagemW.setOpaque(false);
        espacamentoImagemW.setPreferredSize(new Dimension(70, 50));
        holderLblImagem.add(espacamentoImagemW, BorderLayout.WEST);

        JPanel espacamentoImagemE = new JPanel();
        espacamentoImagemE.setOpaque(false);
        espacamentoImagemE.setPreferredSize(new Dimension(80, 50));
        holderLblImagem.add(espacamentoImagemE, BorderLayout.EAST);

        holderLblImagem.add(lblImagem, BorderLayout.CENTER);

        holderBotoesPublicar.add(holderLblImagem, BorderLayout.CENTER);

        //teste de localização e posicionamento
//        espacamentoImagemW.setOpaque(true);
//        espacamentoImagemE.setOpaque(true);
//        espacamentoImagemW.setBackground(Color.red);
//        espacamentoImagemE.setBackground(Color.red);
        holderBotoesPublicar.add(btnUparImagem, BorderLayout.WEST);
        jPanelCentralizarBotoes.add(holderBotoesPublicar, BorderLayout.CENTER);
        holderPublicar.add(jPanelCentralizarBotoes);
        holderPublicar.add(Box.createRigidArea(new Dimension(0, 20)));
        centerContainer.add(holderPublicar, Component.CENTER_ALIGNMENT);

        feedPanel.add(centerContainer);
        centerContainer.add(Box.createRigidArea(new Dimension(0, 10))); // Espaço entre os posts
        for (PublicacaoComum post : posts) {
            // Painel de post com tamanho fixo e centralizado
            PostPanel postPanel = new PostPanel(post);
            postPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            postPanel.setMaximumSize(new Dimension(600, postPanel.getHeight())); // Limita a largura do postPanel
            //feedPanel.setSize(new Dimension(feedPanel.getWidth(), feedPanel.getHeight()+postPanel.getHeight()));
            centerContainer.add(postPanel);
            centerContainer.add(Box.createRigidArea(new Dimension(0, 10))); // Espaço entre os posts
        }

        // Adiciona espaço para centralizar verticalmente
        centerContainer.add(Box.createVerticalGlue());

        // Adiciona o container centralizado ao feedPanel
        //containerPanel.add(centerContainer); // Os posts aqui
        feedPanel.add(centerContainer);

        //feedPanel.add(centerContainer);
        //após adicionar todos os elementos ao feedPanel
        //feedPanel.setPreferredSize(new Dimension(800, feedPanel.getHeight()));
        bodyScrollPane.setMaximumSize(new Dimension(this.getWidth(), feedPanel.getHeight()));
        bodyScrollPane.setViewportView(this.feedPanel);
    }

    private int[] converterHexParaRgb(String hexString) {
        //com 0x no valor hexadecimal: Se a string contém 0x no início:
        //  é necessário tirar o 0x para o int considerar apenas os número
        //  passa-se como parâmetro a base do número (hex = 16)
        int hex = Integer.parseInt(hexString.substring(2), 16);
        int[] retorno = new int[3];

        // Extraindo os componentes RGB
        //hex >> 16: Move RRGGBB para a direita 16 bits, fazendo com que RR esteja nas posições dos 8 bits menos significativos.
        //& 0xFF: Zera quaisquer bits além dos 8 bits menos significativos, isolando o valor de RR.
        retorno[0] = (hex >> 16) & 0xFF; // Vermelho
        //hex >> 8: Move RRGGBB para a direita 8 bits, colocando GG nos 8 bits menos significativos.
        //& 0xFF: Isola o valor de GG.

        retorno[1] = (hex >> 8) & 0xFF;  // Verde
        //Aqui não há deslocamento, porque BB já está nos 8 bits menos significativos.
        //& 0xFF: Isola o valor de BB.

        retorno[2] = hex & 0xFF;         // Azul

        return retorno;
    }
}
