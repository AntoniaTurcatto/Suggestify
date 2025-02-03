package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import modelDominio.Album;
import modelDominio.Artista;
import modelDominio.Comunidade;
import modelDominio.Musica;
import modelDominio.Tag;
import view.tablemodel.AlbumTableModel;
import view.tablemodel.MusicaTableModel;
import view.util.ComboBoxAlbum;
import view.util.ComboBoxTag;
import view.util.Imagem;
import view.util.ListArtistas;
import view.util.RendererListArtistas;

public class TelaGerenciamentoAlbumMusica extends JFrame {

    private int tipoTela; //1= musica; 2=álbum (igual ao tipoConteudo na tabela de sugestões na DB)
    private Comunidade comunidade;
    private JButton btnVoltar, btnMp3Jpg, btnAdicionar, btnExcluir, btnPlay, btnPause;
    private JLabel lblSair, lblTitulo;
    private JTable tblMusicaAlbum;
    private JScrollPane scrollpaneTbl;
    private Musica musicaSelecionada;
    private Album albumSelecionado;
    private ComboBoxAlbum comboModel;
    private AbstractTableModel tableModel;
    private ListArtistas jListModelArtistas;
    private JPanelCustomizado capaAlbum;

    private JLabel lblNome, lblAlbum, lblFeats, lblTag;
    private JTextField txtNome;
    private JComboBox comboAlbum, comboTag;
    private ArrayList<Album> listaAlbums;
    private List<Artista> listFeatsEscolhidos;

    private JFileChooser fileChooser;
    private byte[] arquivoMp3Jpg;
    private Player player;
    private boolean tocando;

    JList<Artista> listFeats;

    public TelaGerenciamentoAlbumMusica(Comunidade comunidade, Integer tipoTela) {
        this.tipoTela = tipoTela.intValue();
        this.comunidade = comunidade;
        fileChooser = new javax.swing.JFileChooser();
        listaAlbums = Principal.ccont.getListaAlbum(this.comunidade.getArtista());
        tocando = false;
        switch (this.tipoTela) {
            case 1:
                this.setTitle("Músicas");
                break;

            case 2:
                this.setTitle("Álbuns");
                break;
            default:
                this.setTitle("[ERRO]");
                break;
        }
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
        banner.setPreferredSize(new Dimension(900, 250));
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

        lblTitulo = new JLabel();
        switch (this.tipoTela) {
            case 1:
                lblTitulo.setText("Músicas");
                break;
            case 2:
                lblTitulo.setText("Álbuns");
                break;
            default:
                lblTitulo.setText("ERRO");
                break;
        }
        lblTitulo.setForeground(new Color(10, 10, 10));
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        lblTitulo.setFont(new Font("Arial", Font.ITALIC, 38));

        holderTextoBanner.add(Box.createRigidArea(new Dimension(0, 8)));

        holderTextoBanner.add(lblTitulo);

        banner.add(holderTextoBanner);

        banner.add(panelMargin2);
        this.add(banner);

        //fim banner======================================
        this.add(Box.createRigidArea(new Dimension(0, 50)));

        //configuração tabela
        switch (this.tipoTela) {
            case 1:
                Object[] listaMusicasEAlbuns = Principal.ccont.listaMusicaComAlbum(this.comunidade.getArtista());
                tableModel = new MusicaTableModel(listaMusicasEAlbuns);
                break;
            case 2:
                tableModel = new AlbumTableModel(listaAlbums);
                break;
            default:
                //fechar tela e abrir menu caso se perca nos tipos de tabela
                TelaComunidade tc = new TelaComunidade(this.comunidade);
                tc.setVisible(true);
                dispose();
                break;
        }

        tblMusicaAlbum = new JTable(tableModel);
        tblMusicaAlbum.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblMusicaAlbum.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                tblMusicaAlbumMouseClicked(evt);
            }
        });

        scrollpaneTbl = new JScrollPane();
        scrollpaneTbl.setPreferredSize(new Dimension(700, 350));
        scrollpaneTbl.setViewportView(tblMusicaAlbum);

        JPanel centralizarTabela = new JPanel(new FlowLayout());
        centralizarTabela.add(scrollpaneTbl);
        centralizarTabela.setOpaque(false);
        this.add(centralizarTabela);
        this.add(Box.createRigidArea(new Dimension(0, 25)));

        //panel para organizar os elementos de gerenciamento horizontalmente
        JPanel horizontalGPane = new JPanel();
        horizontalGPane.setLayout(new BoxLayout(horizontalGPane, BoxLayout.X_AXIS));
        horizontalGPane.setPreferredSize(new Dimension(900, 300));
        horizontalGPane.setOpaque(false);

        //holder para os campos para o usuario preencher a esquerda
        JPanel holderInferiorEsquerdo = new JPanel();
        holderInferiorEsquerdo.setLayout(new BoxLayout(holderInferiorEsquerdo, BoxLayout.Y_AXIS));
        holderInferiorEsquerdo.setPreferredSize(new Dimension(600, 300));
        holderInferiorEsquerdo.setOpaque(false);
        //para centralizar verticalmete os elementos
        holderInferiorEsquerdo.add(Box.createVerticalGlue());

        //os elementos
        //nome
        JPanel nomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        nomePanel.setPreferredSize(new Dimension(500, 20));
        nomePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        nomePanel.setOpaque(false);

        JPanel espacamentoNome = new JPanel();
        espacamentoNome.setOpaque(false);
        espacamentoNome.setPreferredSize(new Dimension(45, 0));
        nomePanel.add(espacamentoNome);

        lblNome = new JLabel("Nome: ");
        lblNome.setForeground(Color.white);
        lblNome.setFont(new Font("Arial", Font.BOLD, 12));
        nomePanel.add(lblNome);

        txtNome = new JTextField();
        txtNome.setPreferredSize(new Dimension(400, 20));
        txtNome.setBackground(new Color(80, 80, 80));
        txtNome.setForeground(Color.white);
        txtNome.setBorder(new EmptyBorder(0, 0, 0, 0));
        nomePanel.add(txtNome);
        holderInferiorEsquerdo.add(nomePanel);
        //fim nome

        if (this.tipoTela == 1) {//musica
            holderInferiorEsquerdo.add(Box.createRigidArea(new Dimension(0, 7)));
            //feat:
            JPanel featPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            featPanel.setPreferredSize(new Dimension(500, 70));
            //featPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
            featPanel.setOpaque(false);

            lblFeats = new JLabel("Colaboradores:");
            lblFeats.setFont(new Font("Arial", Font.BOLD, 12));
            lblFeats.setForeground(Color.white);
            featPanel.add(lblFeats);

            jListModelArtistas = new ListArtistas(Principal.ccont.getListaArtistas());
            listFeats = new JList(jListModelArtistas);
            listFeats.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listFeats.setCellRenderer(new RendererListArtistas());

            //listFeats.setPreferredSize(new Dimension(400, 20));
            listFeats.setBackground(new Color(80, 80, 80));
            listFeats.setForeground(Color.white);
            listFeats.setBorder(new EmptyBorder(0, 0, 0, 0));
            JScrollPane scrollList = new JScrollPane();
            scrollList.setViewportView(listFeats);
            scrollList.setPreferredSize(new Dimension(400, 100));

            featPanel.add(scrollList);
            holderInferiorEsquerdo.add(featPanel);
            //fim feat

            holderInferiorEsquerdo.add(Box.createRigidArea(new Dimension(0, 7)));

            //album
            JPanel albumPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            albumPanel.setPreferredSize(new Dimension(500, 20));
            albumPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
            albumPanel.setOpaque(false);

            JPanel espacamentoAlbum = new JPanel();
            espacamentoAlbum.setOpaque(false);
            espacamentoAlbum.setPreferredSize(new Dimension(45, 0));
            albumPanel.add(espacamentoAlbum);

            lblAlbum = new JLabel("Álbum:");
            lblAlbum.setForeground(Color.white);
            lblAlbum.setFont(new Font("Arial", Font.BOLD, 12));
            albumPanel.add(lblAlbum);

            comboAlbum = new JComboBox();
            comboAlbum.setPreferredSize(new Dimension(400, 20));

            comboAlbum.setBackground(new Color(80, 80, 80));
            comboAlbum.setForeground(Color.white);
            comboAlbum.setBorder(new EmptyBorder(0, 0, 0, 0));

            ComboBoxAlbum.preencheComboBoxMarca(-1, comboAlbum, listaAlbums, false);
            albumPanel.add(comboAlbum);
            holderInferiorEsquerdo.add(albumPanel);

            //fim album
        }//adicionando album
        holderInferiorEsquerdo.add(Box.createRigidArea(new Dimension(0, 7)));
        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        tagPanel.setPreferredSize(new Dimension(500, 20));
        tagPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        tagPanel.setOpaque(false);

        JPanel espacamentoTag = new JPanel();
        espacamentoTag.setOpaque(false);
        espacamentoTag.setPreferredSize(new Dimension(25, 0));
        tagPanel.add(espacamentoTag);

        lblTag = new JLabel("Tag inicial:");
        lblTag.setForeground(Color.white);
        lblTag.setFont(new Font("Arial", Font.BOLD, 12));
        tagPanel.add(lblTag);

        comboTag = new JComboBox();
        comboTag.setPreferredSize(new Dimension(400, 20));

        comboTag.setBackground(new Color(80, 80, 80));
        comboTag.setForeground(Color.white);
        comboTag.setBorder(new EmptyBorder(0, 0, 0, 0));

        ComboBoxTag.preencheComboBoxMarca(-1, comboTag, Principal.ccont.getListaTags(), false);
        tagPanel.add(comboTag);

        holderInferiorEsquerdo.add(tagPanel);

        holderInferiorEsquerdo.add(Box.createVerticalGlue());
        horizontalGPane.add(holderInferiorEsquerdo);

        //holder para os botões a direita
        JPanel holderActions = new JPanel();
        holderActions.setOpaque(false);
        holderActions.setLayout(new BoxLayout(holderActions, BoxLayout.Y_AXIS));
        holderActions.setPreferredSize(new Dimension(300, 300));

        holderActions.add(Box.createVerticalGlue());

        btnMp3Jpg = new JButton();
        btnMp3Jpg.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (this.tipoTela == 1) {
            btnMp3Jpg.setText("Adicionar mp3");
        } else {
            btnMp3Jpg.setText("Adicionar JPG");
        }
        btnMp3Jpg.setBackground(new Color(80, 80, 80));
        btnMp3Jpg.setForeground(Color.white);
        btnMp3Jpg.addActionListener((e) -> {
            btnMp3JpgMouseClicked(e);
        });
        holderActions.add(btnMp3Jpg);

        holderActions.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel centralizarElementos = new JPanel(new FlowLayout());
        centralizarElementos.setOpaque(false);

        if (this.tipoTela == 1) {

            btnPause = new JButton("||");
            btnPause.addActionListener((e) -> {
                btnPauseClicked(e);
            });
            btnPause.setBackground(new Color(80, 80, 80));
            btnPause.setForeground(Color.white);
            btnPause.setPreferredSize(new Dimension(50, 50));
            centralizarElementos.add(btnPause);

            btnPlay = new JButton("|>");
            btnPlay.addActionListener((e) -> {
                btnPlayClicked(e);
            });
            btnPlay.setBackground(new Color(80, 80, 80));
            btnPlay.setForeground(Color.white);
            btnPlay.setPreferredSize(new Dimension(50, 50));
            centralizarElementos.add(btnPlay);
        } else {

            capaAlbum = new JPanelCustomizado(this.comunidade.getArtista().getFotoPerfil(), capaAlbum);
            capaAlbum.setPreferredSize(new Dimension(100, 100));
            capaAlbum.setBackground(new Color(80, 80, 80));
            centralizarElementos.add(capaAlbum);

        }

        holderActions.add(centralizarElementos);

        holderActions.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel centralizarAcoesFinais = new JPanel(new FlowLayout());
        centralizarAcoesFinais.setOpaque(false);

        btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener((e) -> {
            btnExcluirClicado(e);
        });
        btnExcluir.setBackground(new Color(0xDF555C));
        btnExcluir.setForeground(Color.white);

        btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener((e) -> {
            btnAdicionarClicado(e);
        });
        btnAdicionar.setBackground(new Color(0x5755DF));
        btnAdicionar.setForeground(Color.white);

        centralizarAcoesFinais.add(btnExcluir);
        centralizarAcoesFinais.add(btnAdicionar);

        holderActions.add(centralizarAcoesFinais);

        holderActions.add(Box.createVerticalGlue());
        horizontalGPane.add(holderActions);
        this.add(horizontalGPane);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                telaClicada(e);
            }

        });
    }

    public void lblSairClicado(MouseEvent e) {
        System.out.println("Label sair");
        TelaInicial ti = new TelaInicial();
        ti.setVisible(true);
        dispose();
    }

    public void tblMusicaAlbumMouseClicked(MouseEvent evt) {
        switch (this.tipoTela) {
            case 1://musica
                musicaSelecionada = ((MusicaTableModel) tableModel).getMusica(tblMusicaAlbum.getSelectedRow());
                arquivoMp3Jpg = musicaSelecionada.getAudio();
                txtNome.setText(musicaSelecionada.getNome());
                int indexAlbum = 0;
                //percorrendo a lista de albuns
                for (int i = 0; i < listaAlbums.size(); i++) {
                    Album a = listaAlbums.get(i);
                    //verificando se idAlbum do x album da lista
                    //      =
                    //album da musica selecionada
                    if (a.getIdAlbum() == ((MusicaTableModel) tableModel).getAlbum(tblMusicaAlbum.getSelectedRow()).getIdAlbum()) {
                        //armazenando o id da lista de albuns
                        //utilizada no comboBoxAlbum
                        indexAlbum = i;
                    }
                }
                //setando o idEncontrado
                comboAlbum.setSelectedIndex(indexAlbum);
                if (musicaSelecionada.getColaboradores() != null) {
                    if (!musicaSelecionada.getColaboradores().isEmpty()) {

                        //inicializa array com tamanho = lista de colaboradores da musica
                        int[] featsSelecionados = new int[musicaSelecionada.getColaboradores().size()];
                        //contador para contar a posição do array acima e controlar seus inputs
                        int contadorArray = 0;

                        //percorrer a lista de artistas
                        for (int i = 0; i < jListModelArtistas.getElements().size(); i++) {
                            Artista a = jListModelArtistas.getElementAt(i);
                            //e verificar cada um dos artistas do JList
                            for (Artista colaborador : musicaSelecionada.getColaboradores()) {
                                //se o codUsuario deles = o codUsuario de algum dos colaboradores cadastrados na musica
                                if (a.getCodUsuario() == colaborador.getCodUsuario()) {
                                    //se for igual
                                    //array featsSelecionados armazena o indice do JList em que o artista está
                                    featsSelecionados[contadorArray] = i;
                                    //e o contador ja vai para a proxima posição
                                    contadorArray++;
                                }
                            }
                        }
                        //setando os indices selecionados do JList
                        listFeats.setSelectedIndices(featsSelecionados);
                    } else {
                        listFeats.clearSelection();
                    }
                }
                break;
            case 2://album
                albumSelecionado = ((AlbumTableModel) tableModel).getAlbum(tblMusicaAlbum.getSelectedRow());
                arquivoMp3Jpg = albumSelecionado.getImagem();
                txtNome.setText(albumSelecionado.getNome());
                capaAlbum.setBackgroundImage(arquivoMp3Jpg);
                capaAlbum.revalidate();
                capaAlbum.repaint();
                break;
        }
    }

    public void btnMp3JpgMouseClicked(ActionEvent e) {
        System.out.println("btnMp3Jpg clicado");
        FileNameExtensionFilter filter;

        if (this.tipoTela == 1) { //msc
            filter = new FileNameExtensionFilter("Mp3 Files", "mp3");
        } else { //album
            filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        }

        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (this.tipoTela == 1) { //musica

                File selectedFile = fileChooser.getSelectedFile();
                try (FileInputStream fis = new FileInputStream(selectedFile)) {
                    arquivoMp3Jpg = new byte[(int) selectedFile.length()];
                    fis.read(arquivoMp3Jpg);
                    System.out.println("Arquivo lido em bytes: " + arquivoMp3Jpg.length + " bytes.");
                } catch (IOException err) {
                    err.printStackTrace();
                }
            } else { //album
                arquivoMp3Jpg = new Imagem(fileChooser.getSelectedFile()).getImagem();
                System.out.println("imagem encontrada");
                capaAlbum.setBackgroundImage(arquivoMp3Jpg);
                capaAlbum.revalidate();
                capaAlbum.repaint();
            }
        }
    }

    public void btnPauseClicked(ActionEvent e) {
        if (player != null) {
            player.close();
            tocando = false;
            System.out.println("Tocando: " + tocando);
        }
    }

    public void btnPlayClicked(ActionEvent e) {
        System.out.println("Tocando: " + tocando);
        if (arquivoMp3Jpg != null && !tocando) {
            try {
                player = new Player(new ByteArrayInputStream(arquivoMp3Jpg));
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            player.play();

                        } catch (JavaLayerException javaLayerException) {
                            javaLayerException.printStackTrace();
                        }

                    }
                });
                t.start();
                tocando = true;
            } catch (JavaLayerException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void btnVoltarClicado(ActionEvent e) {
        TelaComunidade tc = new TelaComunidade(this.comunidade);
        tc.setLocationRelativeTo(null);
        tc.setVisible(true);
        dispose();
    }

    public void telaClicada(MouseEvent e) {
        if (this.tipoTela == 1) {// listFeats será null se estiver no modo album (2)
            listFeats.clearSelection();
            musicaSelecionada = null;
            arquivoMp3Jpg = null;
        } else {
            //arquivoMp3Jpg não deverá ser null se for album
            arquivoMp3Jpg = this.comunidade.getFotoBackground();
            albumSelecionado = null;
            capaAlbum.setBackgroundImage(arquivoMp3Jpg);
            capaAlbum.revalidate();
            capaAlbum.repaint();
        }
        tblMusicaAlbum.clearSelection();
        txtNome.setText("");
        arquivoMp3Jpg = null;
    }

    public void btnAdicionarClicado(ActionEvent e) {
        System.out.println("botao adicionar clicado");
        switch (this.tipoTela) {
            case 1://musica
                System.out.println("Modo musica");
                if (!txtNome.getText().equals("")) {
                    if (arquivoMp3Jpg != null) {
                        if (ComboBoxTag.getSelectedIndex(comboTag) > 0) {
                            if (ComboBoxAlbum.getSelectedIndex(comboAlbum) > 0) {
                                listFeatsEscolhidos = listFeats.getSelectedValuesList();
                                Musica m;
                                if (!listFeatsEscolhidos.isEmpty()) { //com colaboradores
                                    //public Musica(String nome, byte[] audio, ArrayList<Tag> listaTags, ArrayList<Artista> colaboradores, Artista artistaPrincipal) {
                                    m = new Musica(txtNome.getText(),
                                            arquivoMp3Jpg,
                                            new ArrayList(),
                                            new ArrayList(),
                                            this.comunidade.getArtista());

                                    for (int i = 0; i < listFeatsEscolhidos.size(); i++) {
                                        m.getColaboradores().add(listFeatsEscolhidos.get(i));
                                    }

                                } else {//sem colaboradores
                                    m = new Musica(txtNome.getText(),
                                            arquivoMp3Jpg,
                                            new ArrayList(),
                                            this.comunidade.getArtista());
                                }
                                m.getListaTags().add(new Tag(ComboBoxTag.getSelectedIndex(comboTag)));
                                if (Principal.ccont.insertMusica(m, new Album(ComboBoxAlbum.getSelectedIndex(comboAlbum)))) {
                                    JOptionPane.showMessageDialog(rootPane, "Musica adicionada com sucesso!");
                                    Object[] listaMusicasEAlbuns = Principal.ccont.listaMusicaComAlbum(this.comunidade.getArtista());
                                    tableModel = new MusicaTableModel(listaMusicasEAlbuns);
                                    recarregarTabela(tableModel);
                                } else {
                                    JOptionPane.showMessageDialog(rootPane, "Erro ao adicionar música");
                                }
                            } else {
                                JOptionPane.showMessageDialog(rootPane, "Selecione um álbum");
                            }

                        } else {
                            JOptionPane.showMessageDialog(rootPane, "Selecione uma Tag inicial");
                        }
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Insira um arquivo mp3");
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Insira um nome");
                }

                break;
            case 2://album
                System.out.println("Modo album");
                if (txtNome != null) {
                    if (arquivoMp3Jpg != null) {
                        if (ComboBoxTag.getSelectedIndex(comboTag) > 0) {
                            System.out.println("nome:" + txtNome.getText());
                            //String nome, ArrayList<Tag> listaTags, Artista artista, byte[] imagem
                            Album a = new Album(this.comunidade.getArtista(), txtNome.getText(), arquivoMp3Jpg);
                            a.setListaTags(new ArrayList());
                            a.getListaTags().add(new Tag(ComboBoxTag.getSelectedIndex(comboTag)));
                            if (Principal.ccont.insertAlbum(a)) {
                                JOptionPane.showMessageDialog(rootPane, "Album upado com sucesso!");
                                listaAlbums = Principal.ccont.getListaAlbum(this.comunidade.getArtista());
                                tableModel = new AlbumTableModel(listaAlbums);
                                recarregarTabela(tableModel);
                            } else {
                                JOptionPane.showMessageDialog(rootPane, "Erro no upload do album :(");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Adicione uma imagem para a capa do album");
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Preencha o campo 'nome'");
                }

                break;
            default:
                throw new AssertionError();
        }
    }

    public void btnExcluirClicado(ActionEvent e) {
        switch (this.tipoTela) {
            case 1://musica
                if (musicaSelecionada != null) {
                    if (Principal.ccont.deleteMusica(musicaSelecionada)) {
                        JOptionPane.showMessageDialog(rootPane, "Musica excluida com sucesso!");
                        Object[] listaMusicaEAlbum = Principal.ccont.listaMusicaComAlbum(this.comunidade.getArtista());
                        tableModel = new MusicaTableModel(listaMusicaEAlbum);
                        recarregarTabela(tableModel);
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Erro ao excluir música :(");
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Selecione uma música!");
                }
                break;
            case 2://album
                if (albumSelecionado != null) {
                    if (Principal.ccont.deleteAlbum(albumSelecionado)) {
                        JOptionPane.showMessageDialog(rootPane, "Álbum excluido com sucesso!");
                        listaAlbums = Principal.ccont.getListaAlbum(this.comunidade.getArtista());
                        tableModel = new AlbumTableModel(listaAlbums);
                        recarregarTabela(tableModel);
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Erro ao excluit álbum :(");
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Selecione um álbum!");
                }

                break;
            default:
                throw new AssertionError();
        }
    }

    private void recarregarTabela(AbstractTableModel tableModel) {
        tblMusicaAlbum = new JTable(tableModel);
        this.tableModel = tableModel;
        tblMusicaAlbum.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                tblMusicaAlbumMouseClicked(evt);
            }

        });
        scrollpaneTbl.setViewportView(tblMusicaAlbum);
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
            java.util.logging.Logger.getLogger(TelaMenuTags.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaMenuTags.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaMenuTags.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaMenuTags.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaGerenciamentoAlbumMusica(null, null).setVisible(true);
            }
        });
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
}
