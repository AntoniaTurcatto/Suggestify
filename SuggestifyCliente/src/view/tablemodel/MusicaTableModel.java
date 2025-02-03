package view.tablemodel;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import modelDominio.Album;
import modelDominio.Artista;
import modelDominio.Musica;
import modelDominio.Usuario;

public class MusicaTableModel extends AbstractTableModel {

    private ArrayList<Musica> listaMusicas;
    private ArrayList<Album> listaAlbumDasMusicas;

    public MusicaTableModel(Object[] listaMusicasEAlbum) {
        this.listaMusicas = (ArrayList<Musica>) listaMusicasEAlbum[0];
        this.listaAlbumDasMusicas = (ArrayList<Album>) listaMusicasEAlbum[1];
    }

    @Override
    public int getRowCount() {
        return this.listaMusicas.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Musica m = listaMusicas.get(rowIndex);
        Album a = listaAlbumDasMusicas.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return m.getIdMusica();
            case 1:
                return m.getNome();
            case 2:
                return formatarListaColaboradores(m.getColaboradores());
            case 3:
                return a.getNome();

            default:
                return "[ERRO]";
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "ID";
            case 1:
                return "Nome";
            case 2:
                return "Colaboradores";
            case 3:
                return "Álbum";
            default:
                return "[ERRO]";
        }
    }

    public Musica getMusica(int row) {
        return listaMusicas.get(row);
    }

    public Album getAlbum(int row) {
        return listaAlbumDasMusicas.get(row);
    }

    private String formatarListaColaboradores(ArrayList<Artista> colaboradores) {
        String formatada = "";
        for (Artista a : colaboradores) {
            if (a == colaboradores.getLast()) {
                formatada = formatada + a.getNomeArtistico();
            } else {
                formatada = formatada + a.getNomeArtistico() + ", ";
            }

        }
        return formatada;
    }

}
