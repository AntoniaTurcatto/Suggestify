package view;

import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import modelDominio.Album;
import modelDominio.Comunidade;
import modelDominio.Musica;
import modelDominio.Tag;
import view.tablemodel.AlbumTableModel;
import view.tablemodel.MusicaTableModel;
import view.tablemodel.TagTableModel;

public class TelaGerenciamentoTags extends JFrame {

    private Comunidade comunidade;
    //private TagTableModel tagTableModel;
    private JButton btnVoltar, btnTagsSugeridas, btnAdicionar, btnExcluir;
    private JLabel lblSair, lblTitulo;
    private JTable tblDadosComunidade;
    private JScrollPane scrollpaneTbl;
    private int tipoDeTabela;//0 = tags adicionadas; 1 = todas as tags da DB; 2 = lista de musicas; 3 = lista de tags da musica selecionada; 4 = tags sugeridas

    private Musica musicaSelecionada;
    private Tag tagSelecionada;
    private Album albumSelecionado;
    private AbstractTableModel tableModel;

    public TelaGerenciamentoTags(Comunidade c, Integer tipoDeTabela) {
        this.comunidade = c;
        this.tipoDeTabela = tipoDeTabela.intValue();
        switch (this.tipoDeTabela) {
            //só 0 e 2 são possíveis de ser chamadas direto no construtor
            case 0:
                this.setTitle("Tags de " + c.getArtista().getNomeArtistico());
                break;
            case 2:
                this.setTitle("Músicas");

                break;
            case 7:
                this.setTitle("Álbuns");
                break;
            default:
                this.setTitle("ERRO");
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
        this.setLocationRelativeTo(null);
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

        lblTitulo = new JLabel();
        switch (this.tipoDeTabela) {
            //só 0 e 2 são possíveis de ser chamadas direto no construtor
            case 0:
                lblTitulo.setText("Tags adicionadas");
                break;
            case 2:
                lblTitulo.setText("Músicas");
                break;
            case 7:
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

        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(new Color(0x5755DF));
        menuPanel.setPreferredSize(new Dimension(900, 50));
        menuPanel.setMinimumSize(new Dimension(900, 50));
        menuPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));//utiliza-se o maxValue para a tela se redimensionar

        //panel para organizar os botões a direita
        JPanel panelBotoesADireita = new JPanel();
        panelBotoesADireita.setLayout(new BoxLayout(panelBotoesADireita, BoxLayout.X_AXIS));
        panelBotoesADireita.setOpaque(false);//transparente
        menuPanel.add(panelBotoesADireita, BorderLayout.EAST);

        btnTagsSugeridas = new JButton("Tags sugeridas");
        btnTagsSugeridas.setBackground(new Color(0x2C2C2C));
        btnTagsSugeridas.setFont(new Font("Arial", Font.BOLD, 9));
        btnTagsSugeridas.setPreferredSize(new Dimension(150, 50));
        btnTagsSugeridas.setForeground(Color.white);
        btnTagsSugeridas.addActionListener((ActionEvent e) -> {
            btnTagsSugeridasClicado(e);
        });
        btnTagsSugeridas.setBorderPainted(false);
        panelBotoesADireita.add(btnTagsSugeridas);
        panelBotoesADireita.add(Box.createRigidArea(new Dimension(20, 0)));
        this.add(menuPanel);

        //fim banner======================================
        this.add(Box.createRigidArea(new Dimension(0, 50)));
        //
        switch (this.tipoDeTabela) {
            case 0://COMUNIDADE
                ArrayList<Tag> listaTag = Principal.ccont.getListaTags(c.getArtista());
                tableModel = new TagTableModel(listaTag);
                break;
            case 2://MUSICA
                Object[] listaMusicasEAlbuns = Principal.ccont.listaMusicaComAlbum(this.comunidade.getArtista());
                tableModel = new MusicaTableModel(listaMusicasEAlbuns);
                break;
            case 7: //ALBUM - ----------------------- A FAZER!
                ArrayList<Album> listaAlbuns = Principal.ccont.getListaAlbum(this.comunidade.getArtista());
                tableModel = new AlbumTableModel(listaAlbuns);
                break;
            default:
                //fechar tela e abrir menu caso se perca nos tipos de tabela
                TelaMenuTags tmtg = new TelaMenuTags(comunidade);
                tmtg.setVisible(true);
                dispose();
                break;
        }

        tblDadosComunidade = new JTable(tableModel);
        tblDadosComunidade.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                tblDadosComunidadeMouseClicked(evt);
            }

        });
        scrollpaneTbl = new JScrollPane();
        //scrollpaneTbl.setPreferredSize(new Dimension(300, 350));

        scrollpaneTbl.setViewportView(tblDadosComunidade);

        JPanel centralizarTabela = new JPanel(new FlowLayout());
        centralizarTabela.add(scrollpaneTbl);
        centralizarTabela.setOpaque(false);
        this.add(centralizarTabela);
        this.add(Box.createRigidArea(new Dimension(0, 25)));

        //botoes excluir e adicionar tags
        JPanel holderBotoesTags = new JPanel(new FlowLayout());
        holderBotoesTags.setOpaque(false);
        btnAdicionar = new JButton("Adicionar");
        btnAdicionar.setPreferredSize(new Dimension(125, 38));
        btnAdicionar.setBackground(new Color(0x5755DF));
        btnAdicionar.addActionListener((e) -> {
            btnAdicionarClicado(e);
        });
        btnAdicionar.setForeground(Color.white);

        btnExcluir = new JButton("Excluir");
        btnExcluir.setBackground(new Color(0xDF555C));
        btnExcluir.setPreferredSize(new Dimension(125, 38));
        btnExcluir.addActionListener((e) -> {
            btnExcluirClicado(e);
        });
        btnExcluir.setForeground(Color.white);

        holderBotoesTags.add(btnExcluir);
        holderBotoesTags.add(Box.createRigidArea(new Dimension(190, 0)));
        holderBotoesTags.add(btnAdicionar);
        this.add(holderBotoesTags);

        if (this.tipoDeTabela == 2 || this.tipoDeTabela == 7) {
            //MUSICA E ÁLBUM: não deve ser possível visualizar agora a lista de sugestões de tags
            //visto que não foi selecionada uma musica/álbum específica para tais sugestões
            btnTagsSugeridas.setVisible(false);
            btnTagsSugeridas.setEnabled(false);

            //nem lugar de adicionar ou remover musica
            btnAdicionar.setVisible(false);
            btnAdicionar.setEnabled(false);

            btnExcluir.setVisible(false);
            btnExcluir.setEnabled(false);
        }

        tblDadosComunidade.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDadosComunidadeMouseClicked(evt);
            }

        });
    }

    public void tblDadosComunidadeMouseClicked(MouseEvent evt) {
        //0 = tags adicionadas; 1 = todas as tags da DB (comunidade); 2 = lista de musicas; 
        //3 = lista de tags da musica selecionada;
        //4 = tags sugeridas comunidade; 5 = tags sugeridas para musica; 6 = todas as tags da DB (musica)
        System.out.println("Tipo da tabela: " + this.tipoDeTabela);
        switch (this.tipoDeTabela) {
            //para os casos de selecionar a tag para então excluí-la ou adicioná-la
            case 0, 1, 3, 4, 5, 6, 8, 9, 10:
                tagSelecionada = ((TagTableModel) tableModel).getTag(tblDadosComunidade.getSelectedRow());
                System.out.println("Tag Selecionada: " + tagSelecionada.getTagTexto());
                break;
            //para exibir as tags da musica
            case 2:
                musicaSelecionada = ((MusicaTableModel) tableModel).getMusica(tblDadosComunidade.getSelectedRow());
                this.tipoDeTabela = 3;
                ArrayList<Tag> listaTagsDeMusica = Principal.ccont.getListaTags(musicaSelecionada);
                tableModel = new TagTableModel(listaTagsDeMusica);
                recarregarTabela(tableModel);
                this.setTitle("Tags de " + musicaSelecionada.getNome());
                lblTitulo.setText("Tags adicionadas a " + musicaSelecionada.getNome());
                //pode excluir
                btnExcluir.setVisible(true);
                btnExcluir.setEnabled(true);
                //pode ver a lista de tags sugeridas
                btnTagsSugeridas.setVisible(true);
                btnTagsSugeridas.setEnabled(true);
                //pode ir para a tela de todas as tags da db
                btnAdicionar.setEnabled(true);
                btnAdicionar.setVisible(true);
                break;

            case 7:
                //lista de albuns -> lista de tags do album selecionado
                albumSelecionado = ((AlbumTableModel) tableModel).getAlbum(tblDadosComunidade.getSelectedRow());
                this.tipoDeTabela = 8;
                ArrayList<Tag> listaTagsDeAlbum = Principal.ccont.getListaTags(albumSelecionado);
                tableModel = new TagTableModel(listaTagsDeAlbum);
                recarregarTabela(tableModel);
                this.setTitle("Tags de " + albumSelecionado.getNome());
                lblTitulo.setText("Tags de " + albumSelecionado.getNome());
                //pode excluir
                btnExcluir.setVisible(true);
                btnExcluir.setEnabled(true);
                //pode ver a lista de tags sugeridas
                btnTagsSugeridas.setVisible(true);
                btnTagsSugeridas.setEnabled(true);
                //pode ir para a tela de todas as tags da db
                btnAdicionar.setEnabled(true);
                btnAdicionar.setVisible(true);
                break;
            default:
                //fechar tela e abrir menu caso se perca nos tipos de tabela
                TelaMenuTags tmtg = new TelaMenuTags(comunidade);
                tmtg.setVisible(true);
                dispose();
        }
    }

    public void lblSairClicado(MouseEvent e) {
        System.out.println("Label sair");
        TelaInicial ti = new TelaInicial();
        ti.setVisible(true);
        dispose();
    }

    public void btnVoltarClicado(ActionEvent e) {
        System.out.println("botão Voltar tela comunidades clicado");
        //0 = tags adicionadas; 1 = todas as tags da DB (comunidade); 2 = lista de musicas; 3 = lista de tags da musica selecionada; 4 = tags sugeridas comunidade; 5 = tags sugeridas para musica
        //6 = todas as tags da DB (musica)
        switch (this.tipoDeTabela) {
            //tags add na comunidade e lista de música
            //são as telas iniciais, então ao sair delas, volta ao menu
            case 0, 2, 7 -> {
                TelaMenuTags tmtg = new TelaMenuTags(comunidade);
                tmtg.setVisible(true);
                dispose();

            }
            case 1, 4 -> {
                //1 = todas as tags da DB; 4 = tags sugeridas comunidade
                //são as telas possíveis de serem chamadas da tela de tags add na comunidade
                //então ao sair dessas telas, retorna a de tags adicionadas a comunidade
                this.tipoDeTabela = 0;
                ArrayList<Tag> listaTagsComunidade = Principal.ccont.getListaTags(comunidade.getArtista());
                tableModel = new TagTableModel(listaTagsComunidade);
                recarregarTabela(tableModel);
                this.setTitle("Tags de " + comunidade.getArtista().getNomeArtistico());
                lblTitulo.setText("Tags adicionadas");
                //pode excluir
                btnExcluir.setVisible(true);
                btnExcluir.setEnabled(true);
                //pode ver a lista de tags
                btnTagsSugeridas.setVisible(true);
                btnTagsSugeridas.setEnabled(true);
                //pode voltar a tela 1 = todas as tags da DB (comunidade)
                btnAdicionar.setVisible(true);
                btnAdicionar.setEnabled(true);

            }
            case 3 -> {
                //3 = lista de tags da musica selecionada
                //é a tela possível de ser chamada a partir da tela da lista musicas
                //então ao sair desta, retorna a lista de musicas
                this.tipoDeTabela = 2;
                Object[] listaMusicasEAlbum = Principal.ccont.listaMusicaComAlbum(comunidade.getArtista());
                tableModel = new MusicaTableModel(listaMusicasEAlbum);
                recarregarTabela(tableModel);
                this.setTitle("Músicas de " + comunidade.getArtista().getNomeArtistico());
                lblTitulo.setText("Músicas");

                //não pode excluir musica aqui
                btnExcluir.setVisible(false);
                btnExcluir.setEnabled(false);

                //não pode ir para a lista de tags sugeridas agora
                btnTagsSugeridas.setVisible(false);
                btnTagsSugeridas.setEnabled(false);

                //não pode adicionar musica aqui
                btnAdicionar.setEnabled(false);
                btnAdicionar.setVisible(false);

            }
            case 5, 6 -> {
                //5 = tags sugeridas para musica
                //é a tela possivel de ser chamada na tela de tags ja adicionadas a musica selecionada
                //então, ao sair dela, deve-se retornar a tela da lista de tags já adicionadas a musica selecionada
                this.tipoDeTabela = 3;
                ArrayList<Tag> listaTagsMusicas = Principal.ccont.getListaTags(musicaSelecionada);
                tableModel = new TagTableModel(listaTagsMusicas);
                recarregarTabela(tableModel);
                this.setTitle("Tags de " + musicaSelecionada.getNome());
                lblTitulo.setText("Tags de " + musicaSelecionada.getNome());
                btnExcluir.setVisible(true);
                btnExcluir.setEnabled(true);

                //pode retornar a tabela de tags sugeridas
                btnTagsSugeridas.setVisible(true);
                btnTagsSugeridas.setEnabled(true);

                //pode retornar a tela de todas as tags da DB
                btnAdicionar.setEnabled(true);
                btnAdicionar.setVisible(true);
            }

            case 8 -> {
                //lista de albuns (7) <- lista de tags para o album selecionado
                this.tipoDeTabela = 7;
                ArrayList<Album> listaAlbuns = Principal.ccont.getListaAlbum(this.comunidade.getArtista());
                tableModel = new AlbumTableModel(listaAlbuns);
                recarregarTabela(tableModel);
                this.setTitle("Álbuns");
                lblTitulo.setText("Álbuns");

                //não pode excluir álbuns aqui
                btnExcluir.setVisible(false);
                btnExcluir.setEnabled(false);

                //não pode ver tags sugeridas sem um álbum alvo
                btnTagsSugeridas.setVisible(false);
                btnTagsSugeridas.setEnabled(false);

                //não pode adicionar uma tag a um album não selecionado
                btnAdicionar.setEnabled(false);
                btnAdicionar.setVisible(false);
            }

            case 9, 10 -> {
                //lista de tags ja adicionadas a album (8) <- tags sugeridas a album || todas as tags da DB(album)
                this.tipoDeTabela = 8;
                ArrayList<Tag> listaTagsAlbum = Principal.ccont.getListaTags(albumSelecionado);
                tableModel = new TagTableModel(listaTagsAlbum);
                recarregarTabela(tableModel);
                this.setTitle("Tags de " + albumSelecionado.getNome());
                lblTitulo.setText("Tags de " + albumSelecionado.getNome());

                btnExcluir.setVisible(true);
                btnExcluir.setEnabled(true);

                btnTagsSugeridas.setVisible(true);
                btnTagsSugeridas.setEnabled(true);

                btnAdicionar.setEnabled(true);
                btnAdicionar.setVisible(true);
            }
            default -> {
                //por padrão, fecha a tela e volta ao menu
                TelaMenuTags tmtg = new TelaMenuTags(comunidade);
                tmtg.setVisible(true);
                dispose();
            }
        }

    }

    public void btnTagsSugeridasClicado(ActionEvent e) {
        System.out.println("btnTagsSugeridas Clicado");
        switch (this.tipoDeTabela) {
            case 0:
                this.tipoDeTabela = 4;
                ArrayList<Tag> tagsSugeridas = Principal.ccont.getListaSugestoes(this.comunidade);
                tableModel = new TagTableModel(tagsSugeridas);
                recarregarTabela(tableModel);
                this.setTitle("Tags sugeridas comunidade");
                lblTitulo.setText("Tags sugeridas comunidade");
                //pode excluir
                btnExcluir.setVisible(true);
                btnExcluir.setEnabled(true);
                //ja está na lista de tags sugeridas
                btnTagsSugeridas.setVisible(false);
                btnTagsSugeridas.setEnabled(false);
                //pode aprovar tags
                btnAdicionar.setVisible(true);
                btnAdicionar.setEnabled(true);
                break;
            case 3:
                this.tipoDeTabela = 5;
                ArrayList<Tag> tagsSugeridasMusica = Principal.ccont.getListaSugestoes(musicaSelecionada);
                tableModel = new TagTableModel(tagsSugeridasMusica);
                recarregarTabela(tableModel);
                this.setTitle("Tags sugeridas musica");
                lblTitulo.setText("Tags sugeridas musica");
                //pode excluir
                btnExcluir.setVisible(true);
                btnExcluir.setEnabled(true);
                //ja está na lista de tags sugeridas
                btnTagsSugeridas.setVisible(false);
                btnTagsSugeridas.setEnabled(false);
                //pode aprovar tags
                btnAdicionar.setVisible(true);
                btnAdicionar.setEnabled(true);
                break;

            case 8:
                this.tipoDeTabela = 10;
                ArrayList<Tag> tagsSugeridasAlbum = Principal.ccont.getListaSugestoes(albumSelecionado);
                tableModel = new TagTableModel(tagsSugeridasAlbum);
                recarregarTabela(tableModel);

                this.setTitle("Tags sugeridas álbum");
                lblTitulo.setText("Tags sugeridas álbum");
                //pode excluir
                btnExcluir.setVisible(true);
                btnExcluir.setEnabled(true);
                //ja está na lista de tags sugeridas
                btnTagsSugeridas.setVisible(false);
                btnTagsSugeridas.setEnabled(false);
                //pode aprovar tags
                btnAdicionar.setVisible(true);
                btnAdicionar.setEnabled(true);
                break;

            default:
                //fechar tela e abrir menu caso se perca nos tipos de tabela
                TelaMenuTags tmtg = new TelaMenuTags(comunidade);
                tmtg.setVisible(true);
                dispose();
                break;
        }
    }

    public void btnAdicionarClicado(ActionEvent e) {
        System.out.println("btnAdicionar clicado");
        //0 = tags adicionadas; 1 = todas as tags da DB (comunidade); 2 = lista de musicas; 
        //3 = lista de tags da musica selecionada;
        //4 = tags sugeridas comunidade; 5 = tags sugeridas para musica; 6 = todas as tags da DB (musica)

        switch (this.tipoDeTabela) {
            case 0:
                this.tipoDeTabela = 1;
                ArrayList<Tag> listaTodasTags = Principal.ccont.getListaTags();
                tableModel = new TagTableModel(listaTodasTags);
                recarregarTabela(tableModel);
                this.setTitle("Todas as tags");
                lblTitulo.setText("Todas as tags");
                //não pode excluir
                btnExcluir.setVisible(false);
                btnExcluir.setEnabled(false);
                //não pode ver a lista de tags agora (iria bagunçar o controle de telas)
                btnTagsSugeridas.setVisible(false);
                btnTagsSugeridas.setEnabled(false);
                break;

            case 1:
                if (tagSelecionada != null) {
                    boolean res = Principal.ccont.vincularUsuarioTag(tagSelecionada, Principal.ccont.getUsuarioLogado());
                    if (res) {
                        JOptionPane.showMessageDialog(rootPane, "Tag " + tagSelecionada.getTagTexto() + " adicionada com sucesso!", "Sucesso", 1);
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Erro ao adicionar tag!", "Erro", 3);
                    }
                }

                break;

            case 2, 7:
                System.out.println("NÃO É PARA ISSO ACONTECER, DEVE TIRAR O BOTÃO DESSA TELA, ADICIONAR MUSICA NÃO É NESSA TELA!");
                break;
            case 3:
                //tags musica selecionada -> todas as tags da DB (Musica)
                this.tipoDeTabela = 6;
                ArrayList<Tag> listaTodasTagsCaso3 = Principal.ccont.getListaTags();
                tableModel = new TagTableModel(listaTodasTagsCaso3);
                recarregarTabela(tableModel);
                this.setTitle("Todas as tags");
                lblTitulo.setText("Todas as tags");
                //não pode excluir
                btnExcluir.setVisible(false);
                btnExcluir.setEnabled(false);
                //não pode ver a lista de tags agora (iria bagunçar o controle de telas)
                btnTagsSugeridas.setVisible(false);
                btnTagsSugeridas.setEnabled(false);
                break;

            case 4:
                //validar sugestão de tag para comunidade
                if (tagSelecionada != null) {
                    if (Principal.ccont.validarSugestao(tagSelecionada, comunidade)) {
                        JOptionPane.showMessageDialog(rootPane, "Tag " + tagSelecionada.getTagTexto() + "(sugestão) adicionada com sucesso à comunidade!", "Sucesso", 1);
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Erro ao adicionar tag sugerida!", "Erro", 3);
                    }
                }
                break;

            case 5:
                //validar sugestão de tag para musica
                if (tagSelecionada != null && musicaSelecionada != null) {
                    if (Principal.ccont.validarSugestao(tagSelecionada, musicaSelecionada)) {
                        JOptionPane.showMessageDialog(rootPane, "Tag " + tagSelecionada.getTagTexto() + "(sugestão) adicionada com sucesso à música!", "Sucesso", 1);
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Erro ao adicionar tag sugerida!", "Erro", 3);
                    }
                }
                break;

            case 6:
                //adicionar tag a musica
                if (tagSelecionada != null && musicaSelecionada != null) {
                    if (Principal.ccont.vincularTagGeralNaoSugerida(tagSelecionada, musicaSelecionada)) {
                        JOptionPane.showMessageDialog(rootPane, "Tag " + tagSelecionada.getTagTexto() + " adicionada com sucesso à música!", "Sucesso", 1);
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Erro ao adicionar tag!", "Erro", 3);
                    }
                }

                break;

            case 8:
                //lista de tags adicionadas a album(8) -> lista de todas as tags adicionadas na DB(9)
                this.tipoDeTabela = 9;
                ArrayList<Tag> listaTagsDbAlbum = Principal.ccont.getListaTags();
                tableModel = new TagTableModel(listaTagsDbAlbum);
                recarregarTabela(tableModel);
                this.setTitle("Todas as tags");
                lblTitulo.setText("Todas as tags");
                //não pode excluir
                btnExcluir.setVisible(false);
                btnExcluir.setEnabled(false);
                //não pode ver a lista de tags agora (iria bagunçar o controle de telas)
                btnTagsSugeridas.setVisible(false);
                btnTagsSugeridas.setEnabled(false);

                //btnAdicionar.setVisible(true); 
                //btnAdicionar.setEnabled(true); 
                break;

            case 9:
                //adicionar tag do banco a album
                if (albumSelecionado != null && tagSelecionada != null) {
                    if (Principal.ccont.vincularTagGeralNaoSugerida(tagSelecionada, albumSelecionado)) {
                        JOptionPane.showMessageDialog(rootPane, "Tag " + tagSelecionada.getTagTexto() + " adicionada com sucesso ao album!", "Sucesso", 1);
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Erro ao adicionar tag!", "Erro", 3);
                    }
                }
                break;

            case 10:
                //adicionar tag sugerida a album
                if (albumSelecionado != null && tagSelecionada != null) {
                    if (Principal.ccont.validarSugestao(tagSelecionada, albumSelecionado)) {
                        JOptionPane.showMessageDialog(rootPane, "Tag " + tagSelecionada.getTagTexto() + " (sugestão) adicionada com sucesso ao album!", "Sucesso", 1);
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Erro ao adicionar tag sugerida!", "Erro", 3);
                    }
                }
                break;

            default:
                //fechar tela e abrir menu caso se perca nos tipos de tabela
                TelaMenuTags tmtg = new TelaMenuTags(comunidade);
                tmtg.setVisible(true);
                dispose();
                break;
        }
    }

    public void btnExcluirClicado(ActionEvent e) {
        System.out.println("btnExcluir clicado");
        switch (this.tipoDeTabela) {
            case 0://excluir tag adicionada na comunidade
                if (this.tagSelecionada != null) {
                    if (Principal.ccont.desvincularUsuarioTag(tagSelecionada, comunidade.getArtista())) {
                        JOptionPane.showMessageDialog(rootPane, "Tag " + tagSelecionada.getTagTexto() + " removida com sucesso da comunidade!", "Sucesso", 1);
                        tagSelecionada = null;
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Erro ao remover tag!", "Erro", 3);
                    }
                }
                break;
            case 4: //excluir sugestão de tag para a comunidade
                if (this.tagSelecionada != null) {
                    if (Principal.ccont.excluirSugestao(tagSelecionada, comunidade)) {
                        JOptionPane.showMessageDialog(rootPane, "Tag " + tagSelecionada.getTagTexto() + " removida com sucesso da comunidade!", "Sucesso", 1);
                        tagSelecionada = null;
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Erro ao remover tag sugerida!", "Erro", 3);
                    }
                }
                break;
            case 3://excluir tag adicionada na musica
                if (this.tagSelecionada != null && this.musicaSelecionada != null) {
                    if (Principal.ccont.desvincularTagGeralNaoSugerida(tagSelecionada, this.musicaSelecionada)) {
                        JOptionPane.showMessageDialog(rootPane, "Tag " + tagSelecionada.getTagTexto() + " removida com sucesso da música " + this.musicaSelecionada.getNome() + "!", "Sucesso", 1);
                        tagSelecionada = null;
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Erro ao remover tag!", "Erro", 3);
                    }
                }
                break;
            case 5://excluir sugestão de tag para a musica
                if (this.tagSelecionada != null && this.musicaSelecionada != null) {
                    if (Principal.ccont.excluirSugestao(tagSelecionada, this.musicaSelecionada)) {
                        JOptionPane.showMessageDialog(rootPane, "Tag " + tagSelecionada.getTagTexto() + " (sugestão) removida com sucesso da música " + this.musicaSelecionada.getNome() + "!", "Sucesso", 1);
                        tagSelecionada = null;
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Erro ao remover tag sugerida!", "Erro", 3);
                    }
                }
                break;

            case 8://excluir tag cadastrada de album
                if (this.tagSelecionada != null && this.albumSelecionado != null) {
                    if (Principal.ccont.desvincularTagGeralNaoSugerida(tagSelecionada, albumSelecionado)) {
                        JOptionPane.showMessageDialog(rootPane, "Tag " + tagSelecionada.getTagTexto() + " removida com sucesso do album " + this.albumSelecionado.getNome() + "!", "Sucesso", 1);
                        tagSelecionada = null;
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Erro ao remover tag!", "Erro", 3);
                    }
                }
                break;

            case 10://excluir tag sugerida a album
                if (this.tagSelecionada != null && this.albumSelecionado != null) {
                    if (Principal.ccont.excluirSugestao(tagSelecionada, albumSelecionado)) {
                        JOptionPane.showMessageDialog(rootPane, "Tag " + tagSelecionada.getTagTexto() + " (sugestão) removida com sucesso da música " + this.musicaSelecionada.getNome() + "!", "Sucesso", 1);
                        tagSelecionada = null;
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Erro ao remover tag sugerida!", "Erro", 3);
                    }
                }
                break;
            default:
                //fechar tela e abrir menu caso se perca nos tipos de tabela
                TelaMenuTags tmtg = new TelaMenuTags(comunidade);
                tmtg.setVisible(true);
                dispose();
                break;
        }
    }

    private void recarregarTabela(AbstractTableModel tableModel) {
        tblDadosComunidade = new JTable(tableModel);
        this.tableModel = tableModel;
        tblDadosComunidade.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                tblDadosComunidadeMouseClicked(evt);
            }

        });
        scrollpaneTbl.setViewportView(tblDadosComunidade);
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
                new TelaGerenciamentoTags(null, null).setVisible(true);
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
}
