package model;

import factory.Conector;
import java.sql.*;
import java.util.ArrayList;
import modelDominio.Album;
import modelDominio.Comunidade;
import modelDominio.Musica;
import modelDominio.Tag;
import modelDominio.Usuario;

public class TagDao {

    private Connection con;

    public TagDao() {
        con = Conector.getConnection();
    }

    public ArrayList<Tag> getListaTags() {
        Statement stmt = null;
        ArrayList<Tag> listaTag = new ArrayList<Tag>();
        try {
            stmt = con.createStatement();//utilizado para stmt q não é PreparedStatement
            ResultSet res = stmt.executeQuery("Select * FROM tag");
            while (res.next()) {
                listaTag.add(new Tag(res.getInt("idTag"), res.getString("nome")));
            }
            res.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            listaTag = null;

        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaTag;
    }

    public ArrayList<Tag> getListaTags(Object usuarioMusicaOuAlbum) {

        Object o;
        String sql = "";
        int idConteudo = -1;

        if (usuarioMusicaOuAlbum instanceof Usuario u) {
            o = (Usuario) usuarioMusicaOuAlbum;
            sql = "SELECT tag.* FROM tag"
                    + " INNER JOIN tagsdousuario tgu ON tag.idTag = tgu.fkTag"
                    + " INNER JOIN usuario u ON tgu.fkUsuario = u.idUsuario"
                    + " WHERE u.idUsuario = ?";
            idConteudo = u.getCodUsuario();

        } else if (usuarioMusicaOuAlbum instanceof Musica m) {
            o = (Musica) usuarioMusicaOuAlbum;
            sql = "select tag.* from tag"
                    + " INNER JOIN tagsdemusica tgm ON tag.idTag = tgm.fkTag"
                    + " INNER JOIN musica m on tgm.fkMusica = m.idMusica"
                    + " WHERE m.idMusica = ?";
            idConteudo = m.getIdMusica();

        } else if (usuarioMusicaOuAlbum instanceof Album a) {
            o = (Album) usuarioMusicaOuAlbum;
            sql = "select tag.* from tag"
                    + " INNER JOIN tagsdoalbum tga ON tag.idTag = tga.fkTag"
                    + " INNER JOIN album a on tga.fkAlbum = a.idAlbum"
                    + " WHERE a.idAlbum = ?";
            idConteudo = a.getIdAlbum();

        } else {
            o = null;
            System.out.println("O objeto deve ser do tipo USUARIO, MUSICA OU ALBUM");
        }
        if (idConteudo != -1 && !sql.equals("")) {
            PreparedStatement stmt = null;
            ArrayList<Tag> listaTag = new ArrayList<Tag>();
            System.out.println("Entrou em getListaTags(Comunidade)");
            try {

                stmt = con.prepareStatement(sql);
                stmt.setInt(1, idConteudo);
                ResultSet res = stmt.executeQuery();
                while (res.next()) {
                    listaTag.add(new Tag(res.getInt("idTag"), res.getString("nome")));
                    System.out.println("Encontrou tags");
                }
                res.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                listaTag = null;
            } finally {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return listaTag;
        } else {
            return null;
        }
    }

    
    public boolean inserirTagSemUsuario(Tag tag) {
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            con.setAutoCommit(false);
            String sql = "INSERT INTO tag(idTag,nome) VALUES(null,?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, tag.getTagTexto());
            stmt.execute();
            con.commit();//autoriza a transação
            stmt.close();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean editarTag(Tag tag) {
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            con.setAutoCommit(false);
            String sql = "UPDATE tag set nome = ? WHERE idTag = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, tag.getTagTexto());
            stmt.setInt(2, tag.getIdTag());
            stmt.execute();
            con.commit();
            result = true;
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //terá que excluir de tabelas associativas
    //tagDeUsuario e tagDeMusica
    //e depois da tabela tag
    //PORÉM as tabelas em que ela está como FK estão com ON DELETE CASCADE no constraint
    //ou seja, vão ser excluídas quando solicitada a exclusão do registro pai
    public boolean excluirTag(Tag tag) {
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            con.setAutoCommit(false);
            String sql = "DELETE FROM tag WHERE idTag = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, tag.getIdTag());

            stmt.execute();
            con.commit();
            result = true;
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /*                  SUGESTÃO DE TAGS                          */
    public boolean sugerirTag(Object[] tagTagPaiConteudo) {

        Tag tag;
        Tag tagPai;
        Object conteudo;
        // Valida entradas

        if (tagTagPaiConteudo == null) {
            System.out.println("o array tagTagPaiConteudo não deve ser nulo!");
            return false;
        } else if (tagTagPaiConteudo.length != 3) {
            System.out.println("o array tagTagPaiConteudo deve ter 3 elementos!");
            return false;
        } else if (tagTagPaiConteudo[0] == null || tagTagPaiConteudo[1] == null || tagTagPaiConteudo[2] == null) {
            System.out.println("tag, tagPai ou conteudo não podem ser nulos");
            return false;
        }

        tag = (Tag) tagTagPaiConteudo[0];
        tagPai = (Tag) tagTagPaiConteudo[1];
        conteudo = tagTagPaiConteudo[2];

        //PASSOS:
        // 1:   verificar se a tag ja está na base de dados (se ja estiver, pegar o id dela e NÃO criar uma nova TAG
        //                                                   se NÃO estiver:
        //                                                      1: add a TAG a DB e armazenar o id da mesma
        //                                                      2: add a tag a tabela generoFilho com a respectiva tagPai referenciada)
        // 2:   descobrir o tipo do conteudo( 0 = comunidade; 1 = música; 2 = álbum)
        // 3:   adicionar no DB na tabela sugestoesDeTag com o ID DA TAG e TIPO DE CONTEUDO
        PreparedStatement stmtTagCheck = null;
        PreparedStatement stmtTagCreate = null;
        PreparedStatement stmtGeneroFilho = null;
        PreparedStatement stmtSugestoes = null;

        boolean sucesso = true;

        int idTag = -1;

        //( 0 = comunidade; 1 = música; 2 = álbum)
        //PASSO 2:
        int tipo = (conteudo instanceof Comunidade) ? 0
                : (conteudo instanceof Musica) ? 1
                        : 2;

        try {
            con.setAutoCommit(false);
            //PASSO 1.0
            String sql = "SELECT tag.idTag FROM tag WHERE nome = ?";
            stmtTagCheck = con.prepareStatement(sql);
            stmtTagCheck.setString(1, tag.getTagTexto().toUpperCase());

            ResultSet res = stmtTagCheck.executeQuery();
            //con.commit();
            if (res.next()) {
                idTag = res.getInt("idTag");

            } else {
                //adiciona a tag na tabela tag
                //PASSO 1.1:
                // PASSO 1.1: Inserir a tag na tabela tag
                //fechando ResultSet para prevenir erros e vazamentos de dados
                res.close();
                sql = "INSERT INTO tag(idTag,nome) VALUES(null,?)";
                stmtTagCreate = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmtTagCreate.setString(1, tag.getTagTexto().toUpperCase());
                stmtTagCreate.executeUpdate();
                //con.commit();

                ResultSet generatedKeys = stmtTagCreate.getGeneratedKeys();
                if (generatedKeys.next()) {
                    //inserindo na tabela generoFilho com a ligacao ao genero pai
                    idTag = generatedKeys.getInt(1);
                    //PASSO 1.2:
                    generatedKeys.close();
                    sql = " INSERT INTO generofilho(idGeneroFilho,fkTagGenero,fkGeneroPai)"
                            + " VALUES (null,?,?)";
                    stmtGeneroFilho = con.prepareStatement(sql);
                    stmtGeneroFilho.setInt(1, idTag);
                    stmtGeneroFilho.setInt(2, tagPai.getIdTag());
                    stmtGeneroFilho.execute();
                    //con.commit();

                } else {
                    //erro ao adicionar na tabela tag
                    sucesso = false;
                    System.err.println("ERRO AO ADICIONAR TAG A TABELA TAG");
                }
            }
            //se deu tudo certo até agora
            if (sucesso) {
                //PASSO 3:
                sql = "INSERT INTO sugestoesdetags(idTagSugerida,tipoConteudo,fkTag, fkConteudo)"
                        + " VALUES(null,?,?,?)";
                stmtSugestoes = con.prepareStatement(sql);
                stmtSugestoes.setInt(1, tipo);
                stmtSugestoes.setInt(2, idTag);
                switch (tipo) {
                    case 0:
                        stmtSugestoes.setInt(3, ((Comunidade) conteudo).getIdComunidade());
                        break;
                    case 1:
                        stmtSugestoes.setInt(3, ((Musica) conteudo).getIdMusica());
                        break;
                    case 2:
                        stmtSugestoes.setInt(3, ((Album) conteudo).getIdAlbum());
                        break;
                    default:
                        stmtSugestoes.setInt(3, -1);
                        break;
                }
                stmtSugestoes.execute();

            }
            con.commit();
        } catch (SQLException e) {
            sucesso = false;
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (stmtTagCheck != null) {
                    stmtTagCheck.close();
                }
                if (stmtTagCreate != null) {
                    stmtTagCreate.close();
                }
                if (stmtGeneroFilho != null) {
                    stmtGeneroFilho.close();
                }
                if (stmtSugestoes != null) {
                    stmtSugestoes.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
        return sucesso;
    }

    public boolean excluirSugestao(Object[] tagEConteudo) {

        Tag tag;
        //int idDoConteudo;
        Object conteudo;
        int tipoConteudo;
        int idDoConteudo;

        if (tagEConteudo == null) {
            System.out.println("ERRO: o array tagEIdConteudo não pode ser NULL!");
            return false;
        }
        if (tagEConteudo.length != 2) {
            System.out.println("ERRO: o array tagEIdConteudo deve ter 2 elementos!");
            return false;
        } else if (tagEConteudo[0] == null || tagEConteudo[1] == null) {
            System.out.println("ERRO: tag e conteudo não podem ser NULL!");
            return false;
        } else if (!(tagEConteudo[1] instanceof Comunidade)
                && !(tagEConteudo[1] instanceof Musica)
                && !(tagEConteudo[1] instanceof Album)) {
            System.out.println("ERRO: o conteudo deve ser COMUNIDADE, MUSICA OU ALBUM");
            return false;
        }

        tag = (Tag) tagEConteudo[0];
        conteudo = tagEConteudo[1];

        if (conteudo instanceof Comunidade) {
            tipoConteudo = 0;
            idDoConteudo = ((Comunidade) conteudo).getIdComunidade();
        } else if (conteudo instanceof Musica) {
            tipoConteudo = 1;
            idDoConteudo = ((Musica) conteudo).getIdMusica();
        } else if (conteudo instanceof Album) {
            tipoConteudo = 2;
            idDoConteudo = ((Album) conteudo).getIdAlbum();
        } else {
            tipoConteudo = -1;
            idDoConteudo = -1;
        }

        PreparedStatement stmt = null;
        boolean res;
        try {
            con.setAutoCommit(false);

            String sql = "DELETE FROM sugestoesdetags WHERE fkTag = ? AND fkConteudo = ? AND tipoConteudo = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, tag.getIdTag());
            stmt.setInt(2, idDoConteudo);
            stmt.setInt(3, tipoConteudo);
            stmt.execute();
            con.commit();
            res = true;
        } catch (SQLException e) {
            e.printStackTrace();
            res = false;
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                con.close();
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public ArrayList<Tag> getListaSugestoes(Object conteudo) {
        int tipoConteudo;
        // 0 = comunidade; 1 = música; 2 = álbum
        if (conteudo == null) {
            System.out.println("O CONTEUDO NÃO PODE SER NULL");
            return null;
        } else if (conteudo instanceof Comunidade) {
            tipoConteudo = 0;
        } else if (conteudo instanceof Musica) {
            tipoConteudo = 1;
        } else if (conteudo instanceof Album) {
            tipoConteudo = 2;
        } else {
            //se não é de nenhum tipo permitido
            System.out.println("O CONTEUDO DEVE SER: COMUNIDADE, MUSICA OU ALBUM");
            return null;
        }

        PreparedStatement stmt = null;
        ResultSet res = null;
        ArrayList<Tag> listaTags = new ArrayList();
        try {
            //con.setAutoCommit(true);

            String sql = "SELECT tag.idTag, tag.nome FROM tag"
                    + " INNER JOIN sugestoesdetags sg ON sg.fkTag = tag.idTag"
                    + " WHERE sg.fkConteudo = ? AND sg.tipoConteudo = ?";

            stmt = con.prepareStatement(sql);
            switch (tipoConteudo) {
                // 0 = comunidade; 1 = música; 2 = álbum
                case 0:
                    stmt.setInt(1, ((Comunidade) conteudo).getIdComunidade());
                    break;
                case 1:
                    stmt.setInt(1, ((Musica) conteudo).getIdMusica());
                    break;
                case 2:
                    stmt.setInt(1, ((Album) conteudo).getIdAlbum());
                    break;
                default:
                    System.out.println("O CONTEUDO DEVE SER: COMUNIDADE, MUSICA OU ALBUM");
                    return null;
            }

            stmt.setInt(2, tipoConteudo);

            res = stmt.executeQuery();
            while (res.next()) {
                listaTags.add(new Tag(res.getInt("idTag"), res.getString("nome")));
            }
        } catch (SQLException e) {
            listaTags = null;
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaTags;
    }

    public boolean validarSugestao(Object[] tagEConteudo) {

        Tag tag;
        Object conteudo;
        int tipoConteudo = -1;
        int idConteudo = -1;
        int idComunidadeCasoSeja = -1;

        //PASSO 1: VALIDAÇÃO
        if (tagEConteudo == null) {
            System.out.println("ERRO: o array tagEConteudo não pode ser NULL!");
            return false;

        } else if (tagEConteudo.length != 2) {
            System.out.println("ERRO: o array tagEConteudo deve conter 2 elementos!");
            return false;

        } else if (tagEConteudo[0] == null || tagEConteudo[1] == null) {
            System.out.println("ERRO: os elemenots do array tagEConteudo não podem ser NULL!");
            return false;

        } else if (!(tagEConteudo[0] instanceof Tag)) {
            System.out.println("ERRO: o primeiro elemento do array tagEConteudo deve ser Tag");
            return false;

        }
        tag = (Tag) tagEConteudo[0];
        conteudo = tagEConteudo[1];
        if (conteudo instanceof Comunidade) {
            tipoConteudo = 0;
            idComunidadeCasoSeja = ((Comunidade) conteudo).getIdComunidade();

        } else if (conteudo instanceof Musica) {
            tipoConteudo = 1;
            idConteudo = ((Musica) conteudo).getIdMusica();

        } else if (conteudo instanceof Album) {
            tipoConteudo = 2;
            idConteudo = ((Album) conteudo).getIdAlbum();
        } else {
            System.out.println("ERRO: o segundo elemento do array tagEConteudo deve ser Comunidade, Musica ou Tag! (e apenas um deles!)");
            return false;
        }

        //inicializando as variaveis necessárias após a validação dos dados
        PreparedStatement stmtSelectComunidade = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtDelete = null;

        boolean resultadoDeleteEFinal;

        try {
            con.setAutoCommit(false);
            String sql;
            //PASSO 1.5: DESCOBRIR ID DO ARTISTA DA COMUNIDADE SE tipoConteudo = 0 (comunidade)

            //começa com true para o caso de não ser comunidade (não é necessário descobrir id do artista)
            boolean prontoParaInsert = true;

            if (tipoConteudo == 0) {
                if (idComunidadeCasoSeja > 0) {
                    sql = "SELECT fkUsuarioPrincipal FROM comunidade"
                            + " WHERE idComunidade = ?";
                    stmtSelectComunidade = con.prepareStatement(sql);
                    stmtSelectComunidade.setInt(1, idComunidadeCasoSeja);

                    ResultSet resSelect = stmtSelectComunidade.executeQuery();

                    if (resSelect.next()) {
                        idConteudo = resSelect.getInt("fkUsuarioPrincipal");
                        prontoParaInsert = true;
                    } else {
                        System.err.println("COMUNIDADE de id " + idComunidadeCasoSeja + " NÃO ENCONTRADA");
                        prontoParaInsert = false;
                    }
                } else {
                    System.err.println("ID DA COMUNIDADE NÃO É VÁLIDO (<= 0)");
                    prontoParaInsert = false;
                }
            }

            //PASSO 2: INSERT
            if (prontoParaInsert) {
                switch (tipoConteudo) {
                    case 0: // COMUNIDADE
                        sql = "INSERT INTO tagsdousuario(fkTag,fkUsuario)"
                                + " VALUES(?,?)";
                        break;

                    case 1://MUSICA
                        sql = "INSERT INTO tagsdemusica(fkTag,fkMusica)"
                                + " VALUES(?,?)";
                        break;

                    case 2://ALBUM
                        sql = "INSERT INTO tagsdoalbum(fkTag,fkAlbum)"
                                + " VALUES(?,?)";
                        break;

                    default:
                        sql = "ERRO";
                }
                //insert construído com sucesso
                if (!sql.equals("ERRO")) {
                    stmtInsert = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    stmtInsert.setInt(1, tag.getIdTag());
                    stmtInsert.setInt(2, idConteudo);
                    stmtInsert.execute();

                    ResultSet generatedKeys = stmtInsert.getGeneratedKeys();
                    //verificando se foi gerada uma nova key (insert bem sucedido)
                    if (generatedKeys.next()) {
                        //PASSO 3: DELETE NA TABELA DE SUGESTÕES
                        sql = "DELETE FROM sugestoesdetags WHERE tipoConteudo = ? AND fkTag = ? AND fkConteudo = ?";
                        stmtDelete = con.prepareStatement(sql);
                        stmtDelete.setInt(1, tipoConteudo);
                        stmtDelete.setInt(2, tag.getIdTag());
                        // se é comunidade, é necessário pegar o id da COMUNIDADE
                        if (tipoConteudo == 0) {
                            stmtDelete.setInt(3, idComunidadeCasoSeja);
                        } else {
                            //caso contrário, idConteudo é o fk que queremos
                            stmtDelete.setInt(3, idConteudo);
                        }
                        stmtDelete.execute();
                        resultadoDeleteEFinal = true;
                    } else {
                        System.err.println("INSERT na tabela associativa mal-sucedido");
                        resultadoDeleteEFinal = false;
                    }
                } else {
                    System.err.println("ERRO NO SWITCH: tipoConteudo NÃO É 0,1 ou 2");
                    resultadoDeleteEFinal = false;
                }
            } else {
                System.err.println("INSERT NÃO REALIZADO");
                resultadoDeleteEFinal = false;
            }
            con.commit();
        } catch (SQLException e) {
            resultadoDeleteEFinal = false;
            try {
                con.rollback();
            } catch (SQLException e1) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (stmtInsert != null) {
                    stmtInsert.close();
                }
                if (stmtDelete != null) {
                    stmtDelete.close();
                }
                if (stmtSelectComunidade != null) {
                    stmtSelectComunidade.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultadoDeleteEFinal;
    }

    /*                 VINCULAR TAGS                              */
    public boolean vincularTagGeralNaoSugerida(Object[] tagEConteudo) {
        Tag tag;
        int idConteudo;
        int tipoConteudo;
        if (tagEConteudo == null) {
            System.out.println("ERRO: o array tagEConteudo não pode ser NULL!");
            return false;
        } else if (tagEConteudo.length != 2) {
            System.out.println("ERRO: o array tagEConteudo deve ter 2 elementos!");
            return false;
        } else if (tagEConteudo[0] == null || tagEConteudo[1] == null) {
            System.out.println("ERRO: os elementos do array tagEConteudo não podem ser NULL!");
            return false;
        } else if (!(tagEConteudo[0] instanceof Tag)) {
            System.out.println("ERRO: o primeiro elemento do array tagEConteudo deve ser Tag!");
            return false;
        }

        tag = (Tag) tagEConteudo[0];
        if (tagEConteudo[1] instanceof Comunidade c) {
            tipoConteudo = 0;
            idConteudo = c.getArtista().getCodUsuario();

        } else if (tagEConteudo[1] instanceof Musica m) {
            tipoConteudo = 1;
            idConteudo = m.getIdMusica();

        } else if (tagEConteudo[1] instanceof Album a) {
            tipoConteudo = 2;
            idConteudo = a.getIdAlbum();

        } else if (tagEConteudo[1] instanceof Usuario u) {
            //também é 0 pois é associado na mesma tabela
            tipoConteudo = 0;
            idConteudo = u.getCodUsuario();
        } else {
            tipoConteudo = -1;
            System.out.println("ERRO: o segundo elemento do array tagEConteudo deve ser Comunidade, Musica, Album ou Usuario!");
            return false;
        }

        PreparedStatement stmt = null;
        boolean res;
        try {
            con.setAutoCommit(false);
            String sql;

            switch (tipoConteudo) {
                case 0: // COMUNIDADE ou USUARIO
                    sql = "INSERT INTO tagsdousuario(fkTag,fkUsuario)"
                            + " VALUES(?,?)";
                    break;

                case 1://MUSICA
                    sql = "INSERT INTO tagsdemusica(fkTag,fkMusica)"
                            + " VALUES(?,?)";
                    break;

                case 2://ALBUM
                    sql = "INSERT INTO tagsdoalbum(fkTag,fkAlbum)"
                            + " VALUES(?,?)";
                    break;

                default:
                    sql = "ERRO";
            }

            if (!sql.equals("ERRO")) {

                stmt = con.prepareStatement(sql);
                stmt.setInt(1, tag.getIdTag());
                stmt.setInt(2, idConteudo);
                stmt.execute();
                con.commit();
                res = true;

            } else {
                System.err.println("ERRO no switch em TagDao.vincularTagGeral(): tipo conteudo não é 0,1 ou 2");
                res = false;
            }
        } catch (SQLException e) {
            res = false;
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public boolean desvincularTagGeralNaoSugerida(Object[] tagEConteudo) {
        Tag tag;
        int idConteudo;
        int tipoConteudo;
        if (tagEConteudo == null) {
            System.out.println("ERRO: o array tagEConteudo não pode ser NULL!");
            return false;
        } else if (tagEConteudo.length != 2) {
            System.out.println("ERRO: o array tagEConteudo deve ter 2 elementos!");
            return false;
        } else if (tagEConteudo[0] == null || tagEConteudo[1] == null) {
            System.out.println("ERRO: os elementos do array tagEConteudo não podem ser NULL!");
            return false;
        } else if (!(tagEConteudo[0] instanceof Tag)) {
            System.out.println("ERRO: o primeiro elemento do array tagEConteudo deve ser Tag!");
            return false;
        }

        tag = (Tag) tagEConteudo[0];
        if (tagEConteudo[1] instanceof Comunidade c) {
            tipoConteudo = 0;
            idConteudo = c.getArtista().getCodUsuario();

        } else if (tagEConteudo[1] instanceof Musica m) {
            tipoConteudo = 1;
            idConteudo = m.getIdMusica();

        } else if (tagEConteudo[1] instanceof Album a) {
            tipoConteudo = 2;
            idConteudo = a.getIdAlbum();

        } else if (tagEConteudo[1] instanceof Usuario u) {
            //também é 0 pois é associado na mesma tabela
            tipoConteudo = 0;
            idConteudo = u.getCodUsuario();
        } else {
            tipoConteudo = -1;
            System.out.println("ERRO: o segundo elemento do array tagEConteudo deve ser Comunidade, Musica, Album ou Usuario!");
            return false;
        }

        PreparedStatement stmt = null;
        boolean res;
        try {
            con.setAutoCommit(false);
            String sql;

            switch (tipoConteudo) {
                case 0: // COMUNIDADE ou USUARIO
                    sql = "DELETE FROM tagsdousuario WHERE fkTag = ? AND fkUsuario = ?";
                    break;

                case 1://MUSICA
                    sql = "DELETE FROM tagsdemusica WHERE fkTag = ? AND fkMusica = ?";
                    break;

                case 2://ALBUM
                    sql = "DELETE FROM tagsdoalbum WHERE fkTag = ? AND fkAlbum = ?";
                    break;

                default:
                    sql = "ERRO";
            }

            if (!sql.equals("ERRO")) {

                stmt = con.prepareStatement(sql);
                stmt.setInt(1, tag.getIdTag());
                stmt.setInt(2, idConteudo);
                stmt.execute();
                con.commit();
                res = true;

            } else {
                System.err.println("ERRO no switch em TagDao.desvincularTagGeral(): tipo conteudo não é 0,1 ou 2");
                res = false;
            }
        } catch (SQLException e) {
            res = false;
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public ArrayList<Tag> getTagsRecomendadas(Tag tag) {
        if (tag == null) {
            System.out.println("Tag não pode ser null!");
            return null;
        } else if (tag.getIdTag() <= 0) {
            System.out.println("Tag deve ser já cadastrada");
            return null;
        }

        PreparedStatement stmtGetPai = null, stmtGetFilhos = null;
        ResultSet resPai = null, resFilhos = null;
        boolean generoPai;
        boolean podeGetFilhos;
        ArrayList<Tag> listaTag = new ArrayList();
        int idPai = -1;

        // 8 = id da ultima tag em generoPai
        if (tag.getIdTag() > 8) {
            //ainda não é tagPai e não pode fazer select nos filhos
            generoPai = false;
            podeGetFilhos = false;
        } else {
            //já é pai e pode fazer select nos filhos
            generoPai = true;
            idPai = tag.getIdTag();
            podeGetFilhos = true;
        }

        //if(generoPai)
        //  select nos generos filhos desse pai
        //if !(generoPai)
        //  select no genero pai desse genero filho
        //  select nos generos filhos desse pai
        try {

            String sql = "";
            //tag informada é generoFilho
            if (!generoPai) {
                sql = "SELECT tag.* FROM generofilho gf"
                        + " INNER JOIN generopai gp ON gf.fkGeneroPai = gp.idGeneroPai"
                        + " INNER JOIN tag ON gp.fkTag = tag.idTag"
                        + " WHERE gf.fkTagGenero = ?";
                stmtGetPai = con.prepareStatement(sql);
                stmtGetPai.setInt(1, tag.getIdTag());
                resPai = stmtGetPai.executeQuery();
                if (resPai.next()) {
                    //pai encontrado, pode fazer select nos filhos
                    podeGetFilhos = true;
                    idPai = resPai.getInt("idTag");
                    listaTag.add(new Tag(idPai,
                            resPai.getString("nome")));
                } else {
                    listaTag = null;
                    System.out.println("Tag Pai não encontrada!");
                }
            }
            
            if (podeGetFilhos) {
                sql = "SELECT tag.* FROM generofilho gf"
                        + " INNER JOIN tag ON gf.fkTagGenero = tag.idTag"
                        + " INNER JOIN generopai gp ON gf.fkGeneroPai = gp.idGeneroPai"
                        + " WHERE gp.fkTag = ?";

                stmtGetFilhos = con.prepareStatement(sql);
                System.out.println("idTag do pai:" + idPai);
                stmtGetFilhos.setInt(1, idPai);

                resFilhos = stmtGetFilhos.executeQuery();
                while (resFilhos.next()) {
                    listaTag.add(new Tag(resFilhos.getInt("idTag"),
                            resFilhos.getString("nome")));
                }
            }

        } catch (SQLException e) {
            listaTag = null;
            e.printStackTrace();
        } finally {
            try {
                if (stmtGetPai != null) {
                    stmtGetPai.close();
                }
                if (stmtGetFilhos != null) {
                    stmtGetFilhos.close();
                }
                if (resPai != null) {
                    resPai.close();
                }
                if (resFilhos != null) {
                    resFilhos.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return listaTag;

    }
}
