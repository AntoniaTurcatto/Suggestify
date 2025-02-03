package view.tablemodel;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import modelDominio.Album;

public class AlbumTableModel extends AbstractTableModel {

    private ArrayList<Album> listaAlbuns;

    public AlbumTableModel(ArrayList<Album> listaAlbuns) {
        this.listaAlbuns = listaAlbuns;
    }

    @Override
    public int getRowCount() {
        return listaAlbuns.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: return listaAlbuns.get(rowIndex).getIdAlbum();
            case 1: return listaAlbuns.get(rowIndex).getNome();
            case 2: return listaAlbuns.get(rowIndex).getArtista().getNomeArtistico();
            case 3: return listaAlbuns.get(rowIndex).getImagem();
            default: return "[ERRO]";
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Id";
            case 1: return "Nome";
            case 2: return "Artista";
            case 3: return "Capa";
            default:return "[ERRO]";
        }
    }
    
    public Album getAlbum(int row){
        return listaAlbuns.get(row);
    }
}
